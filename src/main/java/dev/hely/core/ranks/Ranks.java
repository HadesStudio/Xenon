package dev.hely.core.ranks;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.server.Server;
import dev.hely.core.ranks.gson.RankGsonType;
import dev.hely.core.ranks.permissions.RankPermission;
import dev.hely.core.ranks.types.RankType;
import dev.hely.core.utils.ColorUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class Ranks {

    private final Oxygen plugin;
    private final String name;
    private final String uniqueString;

    private boolean defaultRank = false;
    private boolean bungee = false, global = true, syncable;
    private int weight = 1;

    private RankType rankType = RankType.DEFAULT;
    private String prefix = "", suffix = "";

    private List<String> servers = new ArrayList<>();
    private List<RankPermission> permissions = new ArrayList<>();
    private List<String> bungeePermissions = new ArrayList<>();
    private List<String> inheritance = new ArrayList<>();

    private ChatColor color = ChatColor.WHITE, chatColor = ChatColor.WHITE;

    public void save() {
        Document document = new Document();
        document.put("name", this.getName());
        document.put("permissions", Oxygen.PLAIN_GSON.toJson(this.getPermissions(), RankGsonType.RANK_PERMISSION));
        document.put("bungeePermissions", Oxygen.PLAIN_GSON.toJson(this.getBungeePermissions(), RankGsonType.STRING_LIST));
        document.put("prefix", this.getPrefix().replace("§", "&"));
        document.put("suffix", this.getSuffix().replace("§", "&"));
        document.put("color", ColorUtil.convertChatColor(this.getColor()));
        document.put("default", this.isDefaultRank());
        document.put("inheritance", Oxygen.PLAIN_GSON.toJson(this.getInheritance(), RankGsonType.STRING_LIST));
        document.put("weight", this.getWeight());
        document.put("chatColor", ColorUtil.convertChatColor(this.getChatColor()));
        document.put("bungee", this.bungee);
        document.put("servers", Oxygen.PLAIN_GSON.toJson(this.getServers(), RankGsonType.STRING_LIST));
        document.put("global", this.global);
        document.put("type", this.rankType.toString());
        document.put("syncable", this.syncable);

        plugin.getMongoManager().getRanksCollection().replaceOne(Filters.eq("name", this.getName()), document, new UpdateOptions().upsert(true));
    }

    public Ranks load(Document document) {
        if (document == null) {
            return null;
        }

        this.setPermissions(Oxygen.PLAIN_GSON.fromJson(document.getString("permissions"), RankGsonType.RANK_PERMISSION));
        this.setBungeePermissions(Oxygen.PLAIN_GSON.fromJson(document.getString("bungeePermissions"), RankGsonType.STRING_LIST));
        this.setPrefix(document.getString("prefix").replace("§", "&").replace("\u00C2", ""));
        this.setSuffix(document.getString("suffix").replace("§", "&").replace("\u00C2", ""));
        this.setDefaultRank(document.getBoolean("default"));
        this.setInheritance(Oxygen.PLAIN_GSON.fromJson(document.getString("inheritance"), RankGsonType.STRING_LIST));
        this.setWeight(document.getInteger("weight"));
        this.setSyncable(document.containsKey("syncable") && document.getBoolean("syncable"));

        if (document.containsKey("type")) {
            this.setRankType(RankType.valueOf(document.getString("type")));
        } else {
            this.setRankType(RankType.DEFAULT);
        }
        this.setBungee(document.containsKey("bungee") && document.getBoolean("bungee"));
        if (document.containsKey("chatColor")) {
            try {
                this.setChatColor(ChatColor.valueOf(document.getString("chatColor")));
            } catch (Exception ignored) {
                this.setChatColor(ChatColor.WHITE);
            }
        }
        try {
            this.setColor(ChatColor.valueOf(document.getString("color")));
        } catch (Exception e) {
            this.setColor(ChatColor.GRAY);
        }
        if (document.containsKey("global")) {
            this.global = document.getBoolean("global");
        }
        if (document.containsKey("servers")) {
            this.setServers(Oxygen.PLAIN_GSON.fromJson(document.getString("servers"), RankGsonType.STRING_LIST));
        } else {
            this.servers.clear();
        }

        return this;
    }

    public List<String> getAvailablePermissions() {
        return this.permissions.stream().filter(RankPermission::isAvailable).map(RankPermission::getPermission).collect(Collectors.toList());
    }

    public List<String> getAllPermissions() {
        return this.permissions.stream().map(RankPermission::getPermission).collect(Collectors.toList());
    }

    public boolean hasPermission(String value) {
        return this.permissions.stream().filter(RankPermission::isAvailable).filter(permission -> permission.getPermission().equalsIgnoreCase(value)).findFirst().orElse(null) != null;
    }

    public boolean hasPermission(String value, String server) {
        return this.permissions.stream().filter(RankPermission::isAvailable).filter(node -> node.hasServer(server)).filter(permission -> permission.getPermission().equalsIgnoreCase(value)).findFirst().orElse(null) != null;
    }

    public boolean hasBungeePermission(String value) {
        return this.bungeePermissions.stream().filter(permission -> permission.equalsIgnoreCase(value)).findFirst().orElse(null) != null;
    }

    public boolean hasInheritance(String value) {
        return this.inheritance.stream().filter(inheritance -> inheritance.equalsIgnoreCase(value)).findFirst().orElse(null) != null;
    }

    public void removeServer(String name) {
        this.servers.removeIf(server -> server.equalsIgnoreCase(name));
    }

    public boolean hasServer(String name) {
        return this.servers.stream().filter(server -> server.equalsIgnoreCase(name)).findFirst().orElse(null) != null;
    }

    public boolean isAvailableOnServer() {
        return this.global || this.hasServer(Oxygen.getInstance().getConfig().getConfig().getString("plugin-settings.server-name"));
    }

    public boolean isAvailableOnServer(String name) {
        return this.global || this.hasServer(name);
    }

    public boolean isActiveSomewhere() {
        boolean active = false;
        for (Server serverData : plugin.getServerManager().getServerData()) {
            if (isAvailableOnServer(serverData.getServer_name())) {
                active = true;
            }
        }
        return active;
    }
}