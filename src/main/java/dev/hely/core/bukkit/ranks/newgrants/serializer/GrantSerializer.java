package dev.hely.core.bukkit.ranks.newgrants.serializer;

import dev.hely.core.bukkit.modules.database.mongo.serializer.DocumentSerializer;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
import dev.hely.core.bukkit.ranks.newgrants.types.RankGrant;
import dev.hely.core.bukkit.ranks.newgrants.types.TagGrant;
import org.bson.Document;

import java.util.UUID;

public class GrantSerializer implements DocumentSerializer<Grant> {
    public static GrantSerializer INSTANCE = new GrantSerializer();

    @Override
    public Document toDocument(Grant source) {
        Document document = new Document("id", source.getUuid().toString());

        if (source instanceof RankGrant) {
            RankGrant rankGrant = (RankGrant) source;

            document.put("rank-name", rankGrant.getRankName());
        } else if (source instanceof PermissionGrant) {
            PermissionGrant permissionGrant = (PermissionGrant) source;

            document.put("permission", permissionGrant.getPermission());
            document.put("bungeecord", permissionGrant.isBungeecord());
        } else if (source instanceof TagGrant) {
            TagGrant tagGrant = (TagGrant) source;

            document.put("tag-name", tagGrant.getTagName());
        }


        // Default Grant Class Shit:
        document.put("duration", source.getDuration());
        document.put("servers", source.getServers());

        if(source.getAddedBy() != null) {
            document.put("added-by", source.getAddedBy().toString());
        }
        document.put("added-reason", source.getAddedReason());
        document.put("added-at", source.getAddedAt());

        document.put("removed", source.isRemoved());
        if(source.getRemovedBy() != null) {
            document.put("removed-by", source.getRemovedBy().toString());
        }
        document.put("removed-reason", source.getRemovedReason());
        document.put("removed-at", source.getRemovedAt());

        return document;
    }

    @Override
    public Grant fromDocument(Document document) {
        Grant grant = new Grant(UUID.fromString(document.getString("id")));

        if (document.containsKey("rank-name")) {
            grant = new RankGrant(UUID.fromString(document.getString("id")), document.getString("rank-name"));
        } else if (document.containsKey("permission")) {
            grant = new PermissionGrant(UUID.fromString(document.getString("id")), document.getString("permission"), document.getBoolean("bungeecord", false));
        } else if (document.containsKey("tag-name")) {
            grant = new TagGrant(UUID.fromString(document.getString("id")), document.getString("tag-name"));
        }

        grant.setServers(document.getList("servers", String.class));
        grant.setDuration(document.getLong("duration"));

        if(document.containsKey("added-by")) {
            grant.setAddedBy(UUID.fromString(document.getString("added-by")));
        }

        grant.setAddedReason(document.getString("added-reason"));
        grant.setAddedAt(document.getLong("added-at"));

        grant.setRemoved(document.getBoolean("removed"));

        if(document.containsKey("removed-by")) {
            grant.setRemovedBy(UUID.fromString(document.getString("removed-by")));
        }
        grant.setRemovedReason(document.getString("removed-reason"));
        grant.setRemovedAt(document.getLong("removed-at"));

        return grant;
    }
}

