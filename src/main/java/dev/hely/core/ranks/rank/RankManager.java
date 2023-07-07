package dev.hely.core.ranks.rank;

import dev.hely.core.Oxygen;
import dev.hely.core.modules.ModuleLoader;
import dev.hely.core.ranks.gson.GSONUtil;
import dev.hely.core.ranks.rank.permission.PermissionData;
import dev.hely.core.utils.ColorUtil;
import dev.hely.core.utils.PluginUtils;
import dev.hely.lib.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
public class RankManager implements ModuleLoader {
    private boolean enabled;

    @Getter private List<Rank> ranks = new ArrayList<>();
    private Oxygen plugin;

    @Override
    public void enable() {
        this.plugin = Oxygen.getInstance();

        setupRanks();
        this.enabled = true;
    }

    @Override
    public void disable() {
        saveRanks();
        this.enabled = false;
    }

    @Override
    public void reload() {
        saveRanks();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Rank Manager";
    }

    public void setupRanks(){
        ranks.clear();
        Oxygen.getInstance().debug_message("Attempting to setup ranks.", Oxygen.getInstance().getRankManager());

        plugin.getMongoManager().getRanksCollection().find().into(new ArrayList<>()).forEach(document -> {
            Rank rank = new Rank(document.getString("name"));
            rank.load();

            ranks.add(rank);
            Oxygen.getInstance().debug_message("Successfully loaded " + rank.getName() + " rank.", Oxygen.getInstance().getRankManager());
        });

        if (getRanks().size() == 0) {
            this.createRank("Default");
            Oxygen.getInstance().debug_message("Creating the missing default rank because it is non-existent.", Oxygen.getInstance().getRankManager());
        }
    }

    public void saveRanks(){
        ranks.forEach(Rank::save);
        Oxygen.getInstance().debug_message("Saving all rank data.", Oxygen.getInstance().getRankManager());
    }

    public Rank getRank(String name) {
        return this.ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void createRank(String name) {
        Rank new_rank = new Rank(name);
        new_rank.setPrefix("");
        new_rank.setSuffix("");
        new_rank.setDefaultRank(true);
        new_rank.setDisplay_color(ChatColor.LIGHT_PURPLE);
        new_rank.setWeight(1);
        new_rank.setType(RankType.DEFAULT);

        saveRanks();
        this.ranks.add(new_rank);
    }

    public void importRanks() {
        this.ranks.clear();
        Oxygen.getInstance().getRanksConfig().getConfiguration().getKeys(false).forEach(key -> {
            YamlConfiguration config = Oxygen.getInstance().getRanksConfig().getConfiguration();
            Rank rank = new Rank(Oxygen.getInstance().getRanksConfig().getConfiguration().getString(key + ".name"));

            if (config.getString(key + ".permissions-data", null) == null) {
                config.getStringList(key + ".permissions-data").forEach(permission -> {
                    rank.getPermissionsData().add(new PermissionData(permission, true, new ArrayList<>()));
                });
            } else if (PluginUtils.isJson(this.plugin, config.getString(key + ".permissions-data"), GSONUtil.PERMISSION_DATA)) {
                rank.setPermissionsData(Oxygen.PLAIN_GSON.fromJson(config.getString(key + ".permissions-data"), GSONUtil.PERMISSION_DATA));
            } else {
                config.getStringList(key + ".permissions-data").forEach(permission -> {
                    rank.getPermissionsData().add(new PermissionData(permission, true, new ArrayList<>()));
                });
            }
            rank.setBungeePermissions(config.getStringList(key + ".bungee-permissions"));
            rank.setPrefix(config.getString(key + ".prefix"));
            rank.setSuffix(config.getString(key + ".suffix"));
            rank.setDefaultRank(config.getBoolean(key + ".default-rank"));
            rank.setInheritances(config.getStringList(key + ".inheritances"));
            rank.setWeight(config.getInt(key + ".weight"));
            rank.setType(RankType.valueOf(config.getString(key + ".rank-type", "DEFAULT")));
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

    public void saveRanksToConfig() {
        YamlConfiguration file = plugin.getRanksConfig().getConfiguration();
        file.getKeys(false).forEach(key -> file.set(key, null));

        this.ranks.stream().sorted(Comparator.comparingInt(Rank::getWeight).reversed()).forEach(rankData -> {
            file.set(rankData.getName() + ".rank-color", ColorUtil.convertChatColor(rankData.getDisplay_color()));
            file.set(rankData.getName() + ".rank-type", rankData.getType().toString());
            file.set(rankData.getName() + ".prefix", rankData.getPrefix());
            file.set(rankData.getName() + ".default-rank", rankData.isDefaultRank());
            file.set(rankData.getName() + ".global-rank", rankData.isGlobalRank());
            file.set(rankData.getName() + ".weight", rankData.getWeight());
            file.set(rankData.getName() + ".permissions-data", Oxygen.PLAIN_GSON.toJson(rankData.getPermissionsData(), GSONUtil.PERMISSION_DATA));
            file.set(rankData.getName() + ".bungee-permissions", Oxygen.PLAIN_GSON.toJson(rankData.getBungeePermissions(), GSONUtil.listStringType));
            file.set(rankData.getName() + ".inheritances", Oxygen.PLAIN_GSON.toJson(rankData.getInheritances(), GSONUtil.listStringType));
            file.set(rankData.getName() + ".servers", Oxygen.PLAIN_GSON.toJson(rankData.getServers(), GSONUtil.listStringType));

        });
        Oxygen.getInstance().getRanksConfig().save();
    }
}
