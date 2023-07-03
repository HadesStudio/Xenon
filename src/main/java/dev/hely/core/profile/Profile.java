package dev.hely.core.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import lombok.*;
import org.bson.Document;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
public class Profile {

    private final UUID uniqueId;
    private final String name;

    private boolean loaded = false;

    private Long lastSeen;

    private boolean staffChat = false, adminChat = false;

    private Oxygen plugin = Oxygen.getInstance();

    public Profile(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        load();
    }

    private void load() {
        Document document = plugin.getMongoManager().getProfilesCollection().find(Filters.eq("uniqueId", uniqueId.toString())).first();

        if (document != null) {
            lastSeen = document.getLong("lastSeen");
            staffChat = document.getBoolean("staffChat");
            adminChat = document.getBoolean("adminChat");
        }

        loaded = true;
    }

    public void save() {
        save(false);
    }

    public void save(boolean leave) {
        Document document = new Document();
        document.put("uniqueId", uniqueId.toString());
        document.put("name", name);
        document.put("lastSeen", lastSeen);
        document.put("staffChat", staffChat);
        document.put("adminChat", adminChat);

        plugin.getMongoManager().getProfilesCollection().replaceOne(Filters.eq("uniqueId", uniqueId.toString()), document, new ReplaceOptions().upsert(true));
        if (leave) plugin.getProfileManager().getProfileMap().remove(uniqueId);
    }
}
