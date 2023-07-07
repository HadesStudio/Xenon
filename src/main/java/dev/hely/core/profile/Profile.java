package dev.hely.core.profile;


import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import dev.hely.core.profile.note.Note;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.core.ranks.grants.Grant;
import dev.hely.core.profile.permission.ProfilePermission;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.ranks.gson.GSONUtil;
import dev.hely.core.ranks.rank.Rank;
import lombok.*;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

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

    private String currentAddress;
    private List<String> addresses = new ArrayList<>();

    private Long firstSeen;
    private Long lastSeen;

    private String currentServer = "Unknown";
    private String lastServer;

    private List<Punishment> punishments = new ArrayList<>();
    private List<Grant> grants = new ArrayList<>();
    private List<ProfilePermission> profilePermissions = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();

    private List<String> playerPermissions = new ArrayList<>(); // RANK PERMISSIONS
    private List<String> bungeePermissions = new ArrayList<>(); // RANK BUNGEE PERMISSIONS


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
        document.put("currentAddress", currentAddress);
        document.put("addresses", addresses);
        document.put("firstSeen", firstSeen);
        document.put("lastSeen", lastSeen);
        document.put("lastServer", lastServer);
        document.put("tag", tag);
        document.put("staffChat", staffChat);
        document.put("adminChat", adminChat);

        Type grantType = new TypeToken<List<Grant>>() {}.getType();
        document.put("grants", Oxygen.PLAIN_GSON.toJson(this.grants, grantType));

        Type profilePermissionType = new TypeToken<List<ProfilePermission>>() {}.getType();
        document.put("profilePermissions", Oxygen.PLAIN_GSON.toJson(this.profilePermissions, profilePermissionType));

        Type notesType = new TypeToken<List<Note>>() {}.getType();
        document.put("notes", Oxygen.PLAIN_GSON.toJson(this.notes, notesType));


        plugin.getMongoModule().getProfilesCollection().replaceOne(Filters.eq("uniqueId", uniqueId.toString()), document, new ReplaceOptions().upsert(true));
        if (leave) plugin.getProfileManager().getProfileMap().remove(uniqueId);
    }

    private void load() {
        Document document = plugin.getMongoModule().getProfilesCollection().find(Filters.eq("uniqueId", uniqueId.toString())).first();

        if (document != null) {
            if (name == null) {
                name = document.getString("name");
            }

            currentAddress = document.getString("currentAddress");
            addresses = Oxygen.PLAIN_GSON.fromJson(document.getString("addresses"), GSONUtil.listStringType);
            firstSeen = document.getLong("firstSeen");
            lastSeen = document.getLong("lastSeen");
            lastServer = document.getString("lastServer");
            tag = document.getString("tag");
            staffChat = document.getBoolean("staffChat");
            adminChat = document.getBoolean("adminChat");

            Type grantType = new TypeToken<List<Grant>>() {}.getType();
            grants = Oxygen.PLAIN_GSON.fromJson(document.getString("grants"), grantType);
            grants.removeIf(Objects::isNull);

            Type profilePermissionType = new TypeToken<List<ProfilePermission>>() {}.getType();
            profilePermissions = Oxygen.PLAIN_GSON.fromJson(document.getString("profilePermissions"), profilePermissionType);

            Type notesType = new TypeToken<List<Note>>() {}.getType();
            if (document.containsKey("notes")) this.notes = Oxygen.PLAIN_GSON.fromJson(document.getString("notes"), notesType);


            try (MongoCursor<Document> mongoCursor = plugin.getMongoModule().getPunishmentsCollection().find(Filters.eq("targetUUID", uniqueId.toString())).iterator()) {
                mongoCursor.forEachRemaining(punishmentDocument -> {
                    Punishment punishment = new Punishment();

                    punishment.load(punishmentDocument);
                    punishments.add(punishment);
                });
            }
        }

        loaded = true;
    }

    public void setupPermissionAttachments(Player player) {
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null) {
                continue;
            }

            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
                attachmentInfo.getAttachment().unsetPermission(permission);
            });
        }

        PermissionAttachment attachment = player.addAttachment(plugin);
        if (attachment == null) return;
        attachment.getPermissions().keySet().forEach(attachment::unsetPermission);

        List<Grant> activeGrants = new ArrayList<>(this.grants);
        for (Grant grant : activeGrants) {
            if (!grant.isActive()) continue;

            Rank activeRank = plugin.getRankManager().getRank(grant.getRankName());
            if (activeRank != null && activeRank.isServer()) {
                List<String> rankActivePermissions = new ArrayList<>(activeRank.getActivePermissions());

                playerPermissions.addAll(rankActivePermissions);
                bungeePermissions.addAll(activeRank.getBungeePermissions());


                List<String> inheritances = new ArrayList<>(activeRank.getInheritances());
                inheritances.forEach(inheritance -> {
                    Rank rankInheritance = plugin.getRankManager().getRank(inheritance);

                    if (rankInheritance != null && rankInheritance.isServer()) {
                        List<String> inheritanceActivePermissions = new ArrayList<>(rankInheritance.getActivePermissions());

                        playerPermissions.addAll(inheritanceActivePermissions);
                        bungeePermissions.addAll(rankInheritance.getBungeePermissions());
                    }
                });
            }
        }

        this.profilePermissions.removeIf(ProfilePermission::isExpired);

        List<ProfilePermission> activeProfilePermissions = new ArrayList<>(this.profilePermissions);
        activeProfilePermissions.forEach(permission -> {
            if (permission.isActive()) {
                playerPermissions.add(permission.getPermission());
            }
        });



        player.recalculatePermissions();
    }

    public Punishment getActivePunishmentByType(PunishmentType type) {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == type && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }

    public int getPunishmentCountByType(PunishmentType type) {
        int i = 0;

        for (Punishment punishment : punishments) {
            if (punishment.getType() == type) i++;
        }

        return i;
    }

    public Punishment getActiveWarn() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == PunishmentType.WARN && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }

    public Punishment getActiveMute() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == PunishmentType.MUTE && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }

    public Punishment getActiveBan() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == PunishmentType.BAN && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }

    public Punishment getActiveBlacklist() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == PunishmentType.BLACKLIST && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }
}
