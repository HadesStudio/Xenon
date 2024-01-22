package net.ascend.core.bukkit.rank.rank;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.rank.gson.GSONUtil;
import net.ascend.core.bukkit.rank.rank.permission.PermissionData;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.lib.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Xenon
 */
public class RankManager implements ModuleLoader {

    private boolean enabled;

    @Getter private List<Rank> ranks = new ArrayList<>();
    private XenonBukkit plugin;

    @Override
    public void onEnable() {
        this.plugin = XenonBukkit.getInstance();

        setupRanks();
        this.enabled = true;
    }

    @Override
    public void onDisable() {
        saveRanks();
        this.enabled = false;
    }

    @Override
    public void onReload() {
        saveRanks();
    }


    public void setupRanks(){
        ranks.clear();

        plugin.getMongoModule().getRanksCollection().find().into(new ArrayList<>()).forEach(document -> {
            Rank rank = new Rank(document.getString("name"));
            rank.load();

            ranks.add(rank);
        });

        if (getRanks().size() == 0) {
            this.createRank("Default", true);
        }
    }

    public void saveRanks(){
        ranks.forEach(Rank::save);
    }

    public Rank getRank(String name) {
        return this.ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Rank getDefaultRank() {
        List<Rank> ranks = new ArrayList<>(this.ranks).stream().sorted(Comparator.comparingInt(Rank::getWeight).reversed()).filter(Rank::isDefaultRank).collect(Collectors.toList());
        if (ranks.size() == 0) return null;
        return ranks.get(0);
    }

    public void createRank(String name, boolean default_rank) {
        Rank rank = new Rank(name);
        rank.setPrefix("&7[&6" + name + "&7] ");
        rank.setSuffix("");
        rank.setDefaultRank(default_rank);
        rank.setDisplay_color(ChatColor.GOLD);
        rank.setWeight(1);
        if (default_rank) {
            rank.setType(RankType.NORMAL);
        } else {
            rank.setType(RankType.NORMAL);
        }
        rank.setQueuePriority(1);

        this.ranks.add(rank);
        saveRanks();
    }

    public void deleteRank(Rank rank) {
        Document document = plugin.getMongoModule().getRanksCollection().find(Filters.eq("name", rank.getName())).first();
        if (document != null) {
            plugin.getMongoModule().getRanksCollection().deleteOne(document);
        }
        this.ranks.removeIf(rankList -> rankList.getName().equalsIgnoreCase(rank.getName()));
    }


    public void importRanks() {
        this.ranks.clear();
        XenonBukkit.getInstance().getRanksConfig().getConfiguration().getKeys(false).forEach(key -> {
            YamlConfiguration config = XenonBukkit.getInstance().getRanksConfig().getConfiguration();
            Rank rank = new Rank(key);

            if (config.getString(key + ".permissions-data", null) == null) {
                config.getStringList(key + ".permissions-data").forEach(permission -> {
                    rank.getPermissionsData().add(new PermissionData(permission, true, new ArrayList<>()));
                });
            } else if (GSONUtil.isJson(config.getString(key + ".permissions-data"), GSONUtil.PERMISSION_DATA)) {
                rank.setPermissionsData(XenonBukkit.PLAIN_GSON.fromJson(config.getString(key + ".permissions-data"), GSONUtil.PERMISSION_DATA));
            } else {
                config.getStringList(key + ".permissions-data").forEach(permission -> {
                    rank.getPermissionsData().add(new PermissionData(permission, true, new ArrayList<>()));
                });
            }
            rank.setPerks(XenonBukkit.PLAIN_GSON.fromJson(config.getString(key + ".perks"), GSONUtil.RANK_PERKS_DATA));
            rank.setBungeePermissions(config.getStringList(key + ".bungee-permissions"));
            rank.setPrefix(config.getString(key + ".prefix"));
            rank.setSuffix(config.getString(key + ".suffix"));
            rank.setQueuePriority(config.getInt(key + ".queue-priority"));
            rank.setDefaultRank(config.getBoolean(key + ".default-rank"));
            rank.setInheritances(config.getStringList(key + ".inheritances"));
            rank.setWeight(config.getInt(key + ".weight"));
            rank.setType(RankType.valueOf(config.getString(key + ".rank-type", "NORMAL")));
            rank.setGlobalRank(config.getBoolean(key + ".global-rank", true));
            rank.setServers(config.getStringList(key + ".servers"));

            ChatColor color;
            try {
                color = ChatColor.valueOf(config.getString(key + ".rank-color"));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&cFailed to setup &e" + config.getString(key + ".rank-color") + " &cas color, using gray for now."));
                color = ChatColor.GRAY;
            }
            rank.setDisplay_color(color);

            this.ranks.add(rank);
        });
    }

    public Rank getByWeight(int weight) {
        return this.ranks.stream().filter(rank -> rank.getWeight() == weight).findFirst().orElse(null);
    }

    public void saveRanksToConfig() {
        YamlConfiguration file = plugin.getRanksConfig().getConfiguration();
        file.getKeys(false).forEach(key -> file.set(key, null));

        this.ranks.stream().sorted(Comparator.comparingInt(Rank::getWeight).reversed()).forEach(rankData -> {
            file.set(rankData.getName() + ".rank-color", ColorUtil.convertChatColor(rankData.getDisplay_color()));
            file.set(rankData.getName() + ".rank-type", rankData.getType().toString());
            file.set(rankData.getName() + ".prefix", rankData.getPrefix());
            file.set(rankData.getName() + ".suffix", rankData.getSuffix());
            file.set(rankData.getName() + ".default-rank", rankData.isDefaultRank());
            file.set(rankData.getName() + ".global-rank", rankData.isGlobalRank());
            file.set(rankData.getName() + ".queue-priority", rankData.getQueuePriority());
            file.set(rankData.getName() + ".weight", rankData.getWeight());
            file.set(rankData.getName() + ".permissions-data", XenonBukkit.PLAIN_GSON.toJson(rankData.getPermissionsData(), GSONUtil.PERMISSION_DATA));
            file.set(rankData.getName() + ".bungee-permissions", XenonBukkit.PLAIN_GSON.toJson(rankData.getBungeePermissions(), GSONUtil.LIST_STRING_TYPE));
            file.set(rankData.getName() + ".inheritances", XenonBukkit.PLAIN_GSON.toJson(rankData.getInheritances(), GSONUtil.LIST_STRING_TYPE));
            file.set(rankData.getName() + ".servers", XenonBukkit.PLAIN_GSON.toJson(rankData.getServers(), GSONUtil.LIST_STRING_TYPE));
            file.set(rankData.getName() + ".perks", XenonBukkit.PLAIN_GSON.toJson(rankData.getPerks(), GSONUtil.RANK_PERKS_DATA));
        });
        try {
            XenonBukkit.getInstance().getRanksConfig().save();
        } catch (IOException | InvalidConfigurationException e) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
            bukkitCommandSender.sendMessage(CC.translate("&cXenon - Rank Configuration Error"));
            bukkitCommandSender.sendMessage(CC.translate("&c"));
            bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
            bukkitCommandSender.sendMessage(CC.translate("&cError:"));
            e.printStackTrace();
            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
        }
    }
}
