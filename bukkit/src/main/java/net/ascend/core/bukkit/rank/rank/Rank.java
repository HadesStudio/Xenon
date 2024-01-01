package net.ascend.core.bukkit.rank.rank;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.gson.GSONUtil;
import net.ascend.core.bukkit.rank.rank.permission.PermissionData;
import net.ascend.core.bukkit.util.ColorUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Xenon
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Rank {

    private XenonBukkit plugin = XenonBukkit.getInstance();

    private final String name;

    private RankType type = RankType.NORMAL;
    private String prefix = "";
    private String suffix = "";
    private boolean defaultRank = false;
    private boolean globalRank;
    private int weight = 1;
    private List<PermissionData> permissionsData = new ArrayList<>();
    private List<String> bungeePermissions = new ArrayList<>();
    private List<String> inheritances = new ArrayList<>();
    private List<String> servers = new ArrayList<>();
    private ChatColor display_color = ChatColor.WHITE;
    private int queuePriority = 0;

    public void save() {
        Document document = new Document();

        document.put("name", name);
        document.put("type", type.name());
        document.put("prefix", prefix);
        document.put("suffix", suffix);
        document.put("defaultRank", defaultRank);
        document.put("weight", weight);
        document.put("display_color", ColorUtil.convertChatColor(display_color));
        document.put("globalRank", globalRank);
        document.put("queuePriority", queuePriority);


        document.put("permissionsData", XenonBukkit.PLAIN_GSON.toJson(permissionsData, GSONUtil.PERMISSION_DATA));
        document.put("bungeePermissions", XenonBukkit.PLAIN_GSON.toJson(bungeePermissions, GSONUtil.LIST_STRING_TYPE));
        document.put("inheritances", XenonBukkit.PLAIN_GSON.toJson(inheritances, GSONUtil.LIST_STRING_TYPE));
        document.put("servers", XenonBukkit.PLAIN_GSON.toJson(servers, GSONUtil.LIST_STRING_TYPE));

        XenonBukkit.getInstance().getRankManager().saveRanksToConfig();
        plugin.getMongoModule().getRanksCollection().replaceOne(Filters.eq("name", name), document, new ReplaceOptions().upsert(true));
    }

    public void load() {
        Document document = plugin.getMongoModule().getRanksCollection().find(Filters.eq("name", name)).first();

        if (document != null) {
            setType(RankType.valueOf(document.getString("type")));
            setPrefix(document.getString("prefix"));
            setSuffix(document.getString("suffix"));
            setDefaultRank(document.getBoolean("defaultRank"));
            setWeight(document.getInteger("weight"));
            setDisplay_color(ChatColor.valueOf(document.getString("display_color")));
            setGlobalRank(document.getBoolean("globalRank"));
            setQueuePriority(document.getInteger("queuePriority"));
            setPermissionsData(XenonBukkit.PLAIN_GSON.fromJson(document.getString("permissionsData"), GSONUtil.PERMISSION_DATA));
            setBungeePermissions(XenonBukkit.PLAIN_GSON.fromJson(document.getString("bungeePermissions"), GSONUtil.LIST_STRING_TYPE));
            setInheritances(XenonBukkit.PLAIN_GSON.fromJson(document.getString("inheritances"), GSONUtil.LIST_STRING_TYPE));
            setServers(XenonBukkit.PLAIN_GSON.fromJson(document.getString("servers"), GSONUtil.LIST_STRING_TYPE));
        }
    }

    public boolean getServer(String name) {
        return this.servers.stream().filter(server -> server.equalsIgnoreCase(name)).findFirst().orElse(null) != null;
    }

    public boolean isServer() {
        return this.globalRank || getServer(XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
    }

    public PermissionData getPermission(String permission) {
        for(PermissionData rankPermission : permissionsData) {
            if(rankPermission.getPermission().equalsIgnoreCase(permission)) {
                return rankPermission;
            }
        }

        return null;
    }

    public boolean hasPermission(String permission) {
        for(PermissionData rankPermission : permissionsData) {
            if(rankPermission.getPermission().equalsIgnoreCase(permission)) {
                return true;
            }
        }

        for(String parent : getInheritances()) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(parent);

            if (rank.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPermission(PermissionData permission) {
        return hasPermission(permission.getPermission());
    }
}
