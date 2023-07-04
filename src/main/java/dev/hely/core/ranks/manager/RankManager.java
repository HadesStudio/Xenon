package dev.hely.core.ranks.manager;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.manager
//   Date: Tuesday, July 04, 2023 - 4:42 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.client.model.Filters;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.database.redis.packet.implement.rank.GrantsUpdatePacket;
import dev.hely.core.managers.modules.ModuleLoader;
import dev.hely.core.managers.server.global_player.GlobalPlayer;
import dev.hely.core.profile.Profile;
import dev.hely.core.ranks.Ranks;
import dev.hely.core.ranks.grant.Grants;
import dev.hely.core.ranks.grant.events.GrantGivenEvent;
import dev.hely.core.ranks.gson.RankGsonType;
import dev.hely.core.ranks.permissions.RankPermission;
import dev.hely.core.ranks.types.RankType;
import dev.hely.core.utils.ColorUtil;
import dev.hely.core.utils.PluginUtils;
import dev.hely.core.utils.Tasks;
import dev.hely.lib.CC;
import dev.hely.lib.configuration.Config;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class RankManager implements ModuleLoader {
    private Set<Ranks> ranks = new HashSet<>();
    public static final String MODULE_NAME = "Server Manager";
    private boolean enabled;

    @Override
    public void enable() {
        Tasks.runAsync(Oxygen.getInstance(), () -> {
            this.importRanks();
            this.loadRanks();
            this.saveRanks();
            this.saveRanksToConfig();
        });
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    public Ranks getRank(String name) {
        Iterator<Ranks> iterator = this.ranks.iterator();
        if (iterator.hasNext()) {
            do {
                Ranks data = iterator.next();
                if (data.getName().equalsIgnoreCase(name)) {
                    return data;
                }
            } while (iterator.hasNext());
        }
        return null;
    }

    public Ranks getDefaultRank() {
        List<Ranks> ranks = new ArrayList<>(this.ranks).stream().sorted(Comparator.comparingInt(Ranks::getWeight).reversed()).filter(Ranks::isDefaultRank).collect(Collectors.toList());
        if (ranks.size() == 0) {
            return null;
        }
        return ranks.get(0);
    }

    public void deleteRank(Ranks rankData) {
        Document document = Oxygen.getInstance().getMongoManager().getRanksCollection().find(Filters.eq("name", rankData.getName())).first();
        if (document != null) {
            Oxygen.getInstance().getMongoManager().getRanksCollection().deleteOne(document);
        }
        this.ranks.removeIf(rank -> rank.getName().equalsIgnoreCase(rankData.getName()));
    }

    public void saveRanks() {
        ranks.forEach(Ranks::save);
    }

    public void loadRanks() {
        this.ranks.clear();

        Oxygen.getInstance().getMongoManager().getRanksCollection().find().into(new ArrayList<>()).forEach(document -> {
            Ranks rankData = new Ranks(Oxygen.getInstance(), document.getString("name"), PluginUtils.randomString()).load(document);

            if (rankData != null) {
                this.ranks.add(rankData);
            }
        });
        if (this.ranks.size() == 0) {
            this.importRanks();
        }

        Ranks defaultRank = this.getDefaultRank();
        if (defaultRank == null) {
            this.createDefaultRank();
        }
    }

    public void createDefaultRank() {
        Ranks defaultRank = this.getDefaultRank();
        if (defaultRank == null) {
            Ranks rankData = new Ranks(Oxygen.getInstance(), "Default", PluginUtils.randomString());
            rankData.setPrefix("&a");
            rankData.setSuffix("");
            rankData.setDefaultRank(true);
            rankData.setColor(ChatColor.GREEN);
            rankData.setWeight(1);
            rankData.setRankType(RankType.DEFAULT);

            rankData.save();
            this.saveRanksToConfig();

            this.ranks.add(rankData);
        }
    }

    public void importRanks() {
        this.ranks.clear();
        Oxygen.getInstance().getRanksConfig().getConfig().getKeys(false).forEach(key -> {
            Ranks rankData = new Ranks(Oxygen.getInstance(), Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".name"), PluginUtils.randomString());

            if (Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".permissions", null) == null) {
                Oxygen.getInstance().getRanksConfig().getConfig().getStringList(key + ".permissions").forEach(permission -> {
                    rankData.getPermissions().add(new RankPermission(permission, new ArrayList<>(), true));
                });
            } else if (PluginUtils.isJson(Oxygen.getInstance(), Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".permissions"), RankGsonType.RANK_PERMISSION)) {
                rankData.setPermissions(Oxygen.PLAIN_GSON.fromJson(Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".permissions"), RankGsonType.RANK_PERMISSION));
            } else {
                Oxygen.getInstance().getRanksConfig().getConfig().getStringList(key + ".permissions").forEach(permission -> {
                    rankData.getPermissions().add(new RankPermission(permission, new ArrayList<>(), true));
                });
            }
            rankData.setBungeePermissions(Oxygen.getInstance().getRanksConfig().getConfig().getStringList(key + ".bungee-permissions"));
            rankData.setPrefix(Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".prefix"));
            rankData.setSuffix(Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".suffix"));
            rankData.setDefaultRank(Oxygen.getInstance().getRanksConfig().getConfig().getBoolean(key + ".default-rank"));
            rankData.setInheritance(Oxygen.getInstance().getRanksConfig().getConfig().getStringList(key + ".inheritance"));
            rankData.setWeight(Oxygen.getInstance().getRanksConfig().getConfig().getInt(key + ".weight"));
            rankData.setBungee(Oxygen.getInstance().getRanksConfig().getConfig().getBoolean(key + ".bungee"));
            rankData.setRankType(RankType.valueOf(Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".type", "DEFAULT")));
            rankData.setGlobal(Oxygen.getInstance().getRanksConfig().getConfig().getBoolean(key + ".global", true));
            rankData.setServers(Oxygen.getInstance().getRanksConfig().getConfig().getStringList(key + ".servers"));

            ChatColor color;
            try {
                color = ChatColor.valueOf(Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".rank-color"));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&cFailed to set &e" + Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".rank-color") + " &cas color, using gray for now."));
                color = ChatColor.GRAY;
            }
            rankData.setColor(color);

            ChatColor chatColor;
            try {
                chatColor = ChatColor.valueOf(Oxygen.getInstance().getRanksConfig().getConfig().getString(key + ".chat-color"));
            } catch (Exception ignored) {
                chatColor = ChatColor.WHITE;
            }
            rankData.setChatColor(chatColor);

            this.ranks.add(rankData);
        });
    }

    public void saveRanksToConfig() {
        Config file = Oxygen.getInstance().getRanksConfig();
        file.getConfiguration().getKeys(false).forEach(key -> file.getConfig().set(key, null));

        this.ranks.stream().sorted(Comparator.comparingInt(Ranks::getWeight).reversed()).forEach(rankData -> {
            file.getConfig().set(rankData.getName() + ".name", rankData.getName());
            file.getConfig().set(rankData.getName() + ".prefix", rankData.getPrefix().replace("§", "&"));
            file.getConfig().set(rankData.getName() + ".suffix", rankData.getSuffix().replace("§", "&"));
            file.getConfig().set(rankData.getName() + ".default-rank", rankData.isDefaultRank());
            file.getConfig().set(rankData.getName() + ".rank-color", ColorUtil.convertChatColor(rankData.getColor()));
            file.getConfig().set(rankData.getName() + ".permissions", Oxygen.PLAIN_GSON.toJson(rankData.getPermissions(), RankGsonType.RANK_PERMISSION));
            file.getConfig().set(rankData.getName() + ".inheritance", rankData.getInheritance());
            file.getConfig().set(rankData.getName() + ".weight", rankData.getWeight());
            file.getConfig().set(rankData.getName() + ".chat-color", ColorUtil.convertChatColor(rankData.getChatColor()));
            file.getConfig().set(rankData.getName() + ".bungee", rankData.isBungee());
            file.getConfig().set(rankData.getName() + ".bungee-permissions", rankData.getBungeePermissions());
            file.getConfig().set(rankData.getName() + ".type", rankData.getRankType().toString());
            file.getConfig().set(rankData.getName() + ".global", rankData.isGlobal());
            file.getConfig().set(rankData.getName() + ".servers", rankData.getServers());
        });
        file.save();
    }

    public void giveRank(CommandSender sender, Profile data, long duration, boolean permanent, String reason, Ranks rankData, String server) {
        Grants grant = new Grants();

        grant.setRankName(rankData.getName());
        grant.setActive(true);
        grant.setAddedAt(System.currentTimeMillis());
        grant.setAddedBy(sender.getName());
        grant.setDuration(duration);
        grant.setPermanent(permanent);
        grant.setReason(reason);
        grant.setServer(server);

        GrantGivenEvent event = new GrantGivenEvent(grant, data, sender);
        Oxygen.getInstance().getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        Tasks.runAsync(Oxygen.getInstance(), () -> {
            data.getGrants().add(grant);

            GlobalPlayer globalPlayer = Oxygen.getInstance().getServerManager().getGlobalPlayer(data.getName());
            if (grant.isPermanent()) {
                sender.sendMessage("granted permission!");
                if (globalPlayer != null) {
                    globalPlayer.sendMessage("you where given a permission wow");
                }
            } else {
                sender.sendMessage("given temporary rank grant");
                if (globalPlayer != null) {
                    globalPlayer.sendMessage("you where given a grant rank! omg wow");
                }
            }
            data.save();

            Player target = Bukkit.getPlayer(data.getName());
            if (target == null) {
                new GrantsUpdatePacket(data.getUniqueId(), data.getGrants()).send();
            } else {
                Profile playerData = Oxygen.getInstance().getProfileManager().getProfile(target.getUniqueId());
                playerData.loadAttachments(target);
            }
        });
    }

    public void removeRank(CommandSender sender, Profile data, String rankName) {
        data.getGrants().stream().filter(grant -> grant.getRankName().equalsIgnoreCase(rankName)).forEach(grant -> {
            grant.setRemovedBy(sender.getName());
            grant.setRemovedAt(System.currentTimeMillis());
            grant.setActive(false);
        });

        GlobalPlayer globalPlayer = Oxygen.getInstance().getServerManager().getGlobalPlayer(data.getName());

        sender.sendMessage("grant removed!!");
        if (globalPlayer != null) {
            globalPlayer.sendMessage("your grant rank has been removedddd");
        }

        data.save();

        Player target = Bukkit.getPlayer(data.getName());
        if (target == null) {
            new GrantsUpdatePacket(data.getUniqueId(), data.getGrants()).send();
        } else {
            Profile playerData = Oxygen.getInstance().getProfileManager().getProfile(target.getUniqueId());
            playerData.loadAttachments(target);
        }
        Oxygen.getInstance().getProfileManagement().deleteData(data.getUniqueId());
    }

    public boolean canGrant(Profile granter, Ranks rankData) {
        Ranks granterRank = granter.getHighestRank();
        return granterRank.getWeight() > rankData.getWeight();
    }
}
