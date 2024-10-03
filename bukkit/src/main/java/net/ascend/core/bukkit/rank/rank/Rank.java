package net.ascend.core.bukkit.rank.rank;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.gson.GSONUtil;
import net.ascend.core.bukkit.rank.rank.permission.PermissionData;
import net.ascend.core.bukkit.util.ColorUtil;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> inheritances = new ArrayList<>();
    private List<String> servers = new ArrayList<>();
    private ChatColor display_color = ChatColor.WHITE;

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
        document.put("permissionsData", XenonBukkit.PLAIN_GSON.toJson(permissionsData, GSONUtil.PERMISSION_DATA));
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
            setPermissionsData(XenonBukkit.PLAIN_GSON.fromJson(document.getString("permissionsData"), GSONUtil.PERMISSION_DATA));
            setInheritances(XenonBukkit.PLAIN_GSON.fromJson(document.getString("inheritances"), GSONUtil.LIST_STRING_TYPE));
            setServers(XenonBukkit.PLAIN_GSON.fromJson(document.getString("servers"), GSONUtil.LIST_STRING_TYPE));
        }
    }

    public boolean getServer(String name) {
        return this.servers.stream().filter(server -> server.equalsIgnoreCase(name)).findFirst().orElse(null) != null;
    }

    public void removeServer(String name) {
        this.servers.removeIf(server -> server.equalsIgnoreCase(name));
    }

    public boolean isOnServer() {
        return this.globalRank || getServer(XenonBukkit.getInstance().getServerManager().getCurrentServerName());
    }

    public boolean isOnServer(String server) {
        return this.globalRank || getServer(server);
    }

    public boolean hasPermission(String value) {
        return this.permissionsData.stream().filter(PermissionData::isAvailable).filter(permission -> permission.getPermission().equalsIgnoreCase(value)).findFirst().orElse(null) != null;
    }
}
