package dev.hely.core.ranks.rank;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import dev.hely.core.ranks.gson.GSONUtil;
import dev.hely.core.ranks.rank.permission.PermissionData;
import dev.hely.core.utils.ColorUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Rank {

    private Oxygen plugin = Oxygen.getInstance();

    private final String name;
    private RankType type = RankType.NORMAL;
    private String prefix, suffix;
    private boolean defaultRank;
    private boolean globalRank;
    private int weight = 1;
    private List<PermissionData> permissionsData = new ArrayList<>();
    private List<String> bungeePermissions = new ArrayList<>();
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


        document.put("permissionsData", Oxygen.PLAIN_GSON.toJson(permissionsData, GSONUtil.PERMISSION_DATA));
        document.put("bungeePermissions", Oxygen.PLAIN_GSON.toJson(bungeePermissions, GSONUtil.LIST_STRING_TYPE));
        document.put("inheritances", Oxygen.PLAIN_GSON.toJson(inheritances, GSONUtil.LIST_STRING_TYPE));
        document.put("servers", Oxygen.PLAIN_GSON.toJson(servers, GSONUtil.LIST_STRING_TYPE));

        Oxygen.getInstance().getRankManager().saveRanksToConfig();
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

            Type permissionType = new TypeToken<List<PermissionData>>() {}.getType();
            setPermissionsData(Oxygen.PLAIN_GSON.fromJson(document.getString("permissionsData"), permissionType));
            setBungeePermissions(Oxygen.PLAIN_GSON.fromJson(document.getString("bungeePermissions"), GSONUtil.LIST_STRING_TYPE));
            setInheritances(Oxygen.PLAIN_GSON.fromJson(document.getString("inheritances"), GSONUtil.LIST_STRING_TYPE));
            setServers(Oxygen.PLAIN_GSON.fromJson(document.getString("servers"), GSONUtil.LIST_STRING_TYPE));
        }
    }

    public boolean getServer(String name) {
        return this.servers.stream().filter(server -> server.equalsIgnoreCase(name)).findFirst().orElse(null) != null;
    }

    public boolean isServer() {
        return this.globalRank || getServer(plugin.getServerManager().getServerName());
    }

    public List<String> getActivePermissions() {
        return this.permissionsData.stream().filter(PermissionData::isActive).map(PermissionData::getPermission).collect(Collectors.toList());
    }
}
