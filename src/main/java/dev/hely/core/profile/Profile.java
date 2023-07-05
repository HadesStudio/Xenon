package dev.hely.core.profile;


import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import dev.hely.core.leandro.grants.Grant;
import dev.hely.core.leandro.rank.permission.PermissionData;
import dev.hely.core.profile.permission.ProfilePermission;
import dev.hely.core.punishment.Punishment;
import lombok.*;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
public class Profile {

    private final UUID uniqueId;
    private String name;

    private boolean loaded = false;

    private Long lastSeen;

    private List<Punishment> punishments = new ArrayList<>();
    private List<Grant> grants = new ArrayList<>();
    private List<ProfilePermission> profilePermissions = new ArrayList<>();

    private String tag;

    private boolean staffChat = false, adminChat = false;

    private Oxygen plugin = Oxygen.getInstance();

    public Profile(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        load();
    }

    public void save() {
        save(false);
    }

    public void save(boolean leave) {
        Document document = new Document();
        document.put("uniqueId", uniqueId.toString());
        document.put("name", name);
        document.put("lastSeen", lastSeen);
        document.put("tag", tag);
        document.put("staffChat", staffChat);
        document.put("adminChat", adminChat);

        Type grantType = new TypeToken<List<Grant>>() {}.getType();
        document.put("grants", Oxygen.PLAIN_GSON.toJson(this.grants, grantType));

        Type profilePermissionType = new TypeToken<List<ProfilePermission>>() {}.getType();
        document.put("profilePermissions", Oxygen.PLAIN_GSON.toJson(this.profilePermissions, profilePermissionType));

        plugin.getMongoManager().getProfilesCollection().replaceOne(Filters.eq("uniqueId", uniqueId.toString()), document, new ReplaceOptions().upsert(true));
        if (leave) plugin.getProfileManager().getProfileMap().remove(uniqueId);
    }

    private void load() {
        Document document = plugin.getMongoManager().getProfilesCollection().find(Filters.eq("uniqueId", uniqueId.toString())).first();

        if (document != null) {
            name = document.getString("name");
            lastSeen = document.getLong("lastSeen");
            tag = document.getString("tag");
            staffChat = document.getBoolean("staffChat");
            adminChat = document.getBoolean("adminChat");

            Type grantType = new TypeToken<List<Grant>>() {}.getType();
            grants = Oxygen.PLAIN_GSON.fromJson(document.getString("grants"), grantType);
            grants.removeIf(Objects::isNull);

            Type profilePermissionType = new TypeToken<List<ProfilePermission>>() {}.getType();
            profilePermissions = Oxygen.PLAIN_GSON.fromJson(document.getString("profilePermissions"), profilePermissionType);


            try (MongoCursor<Document> mongoCursor = plugin.getMongoManager().getPunishmentsCollection().find(Filters.eq("targetUUID", uniqueId.toString())).iterator()) {
                mongoCursor.forEachRemaining(punishmentDocument -> {
                    Punishment punishment = new Punishment();

                    punishment.load(punishmentDocument);
                    punishments.add(punishment);
                });
            }
        }

        loaded = true;
    }
}
