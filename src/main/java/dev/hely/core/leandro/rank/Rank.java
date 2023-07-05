package dev.hely.core.leandro.rank;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import dev.hely.core.leandro.gson.GSONUtil;
import dev.hely.core.leandro.rank.permission.PermissionData;
import dev.hely.lib.CC;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Rank {

    private final String name;

    private RankType type = RankType.NORMAL;
    private String prefix, suffix;
    private boolean defaultRank;
    private int weight;

    private List<PermissionData> permissionsData = new ArrayList<>();
    private List<String> bungeePermissions = new ArrayList<>();

    private List<String> inheritances = new ArrayList<>();

    private Oxygen plugin = Oxygen.getInstance();

    public void save() {
        Document document = new Document();

        document.put("type", type.name());
        document.put("prefix", prefix);
        document.put("suffix", suffix);
        document.put("defaultRank", defaultRank);
        document.put("weight", weight);

        Type permissionType = new TypeToken<List<PermissionData>>() {}.getType();
        document.put("permissionsData", Oxygen.PLAIN_GSON.toJson(permissionsData, permissionType));
        document.put("bungeePermissions", Oxygen.PLAIN_GSON.toJson(bungeePermissions, GSONUtil.listStringType));
        document.put("inheritances", Oxygen.PLAIN_GSON.toJson(inheritances, GSONUtil.listStringType));

        plugin.getMongoManager().getRanksCollection().replaceOne(Filters.eq("name", name), document, new ReplaceOptions().upsert(true));
    }

    public void load() {
        Document document = plugin.getMongoManager().getRanksCollection().find(Filters.eq("name", name)).first();

        if (document != null) {
            setType(RankType.valueOf(document.getString("type")));
            setPrefix(document.getString("prefix"));
            setSuffix(document.getString("suffix"));
            setDefaultRank(document.getBoolean("defaultRank"));
            setWeight(document.getInteger("weight"));

            Type permissionType = new TypeToken<List<PermissionData>>() {}.getType();
            setPermissionsData(Oxygen.PLAIN_GSON.fromJson(document.getString("permissionsData"), permissionType));
            setBungeePermissions(Oxygen.PLAIN_GSON.fromJson(document.getString("bungeePermissions"), GSONUtil.listStringType));
            setInheritances(Oxygen.PLAIN_GSON.fromJson(document.getString("inheritances"), GSONUtil.listStringType));
        }
    }
}
