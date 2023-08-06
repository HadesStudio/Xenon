package dev.hely.core.bukkit.profile;


import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.packet.implement.StaffChatPacket;
import dev.hely.core.bukkit.profile.permission.packet.PlayerPermissionPacket;
import dev.hely.core.bukkit.profile.profilesystem.impl.*;
import dev.hely.core.bukkit.profile.staff.StaffProfile;
import dev.hely.core.bukkit.profile.note.Note;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.Statistics;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.StatisticsServer;
import dev.hely.core.bukkit.profile.profilesystem.impl.social.SocialMediaType;
import dev.hely.core.bukkit.profile.siblings.Sibling;
import dev.hely.core.bukkit.punishment.PunishmentType;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.profile.permission.ProfilePermission;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.ranks.gson.GSONUtil;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.ranks.rank.RankType;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.lib.CC;
import lombok.*;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name OxygenGlobal
 */
@Getter
@Setter
public class Profile {

    private final UUID uniqueId;
    private String name;
    private boolean loaded = false;
    private String currentAddress = "";
    private List<String> addresses = new ArrayList<>();
    private Long firstSeen;
    private Long lastSeen;
    private String currentServer = "Unknown";
    private String lastServer = "";
    private int totalKills;
    private int totalDeaths;
    private List<Punishment> punishments = new ArrayList<>();
    private List<Grant> grants = new ArrayList<>();
    private List<ProfilePermission> profilePermissions = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private List<String> playerPermissions = new ArrayList<>(); // RANK PERMISSIONS
    private List<String> bungeePermissions = new ArrayList<>(); // RANK BUNGEE PERMISSIONS
    //For notifications / mail system / profile system:
    private List<Notification> notifications = new ArrayList<>();
    private List<NotificationType> notification_settings = new ArrayList<>();
    private List<UUID> friends = new ArrayList<>();
    private List<UUID> friendRequests = new ArrayList<>();
    private Map<StatisticsServer, Statistics> statistics = new HashMap<>();
    private Map<SocialMediaType, String> socialMedia = new HashMap<>();
    private Preferences preferences;
    private List<Comment> comments = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private boolean vpnWhitelist = false;
    private String tag = "";
    private StaffProfile staffProfile = new StaffProfile();
    private OxygenBukkit plugin = OxygenBukkit.getInstance();

    private List<Sibling> siblings = new ArrayList<>();


    public Profile(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        this.totalKills = 0;
        this.totalDeaths = 0;

        for (StatisticsServer value : StatisticsServer.values()) {
            statistics.put(value, new Statistics(value));
        }

        this.preferences = new Preferences();

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
        document.put("addresses", OxygenBukkit.PLAIN_GSON.toJson(addresses, GSONUtil.LIST_STRING_TYPE));
        document.put("firstSeen", firstSeen);
        document.put("lastSeen", lastSeen);
        document.put("lastServer", lastServer);
        document.put("vpnWhitelist", vpnWhitelist);
        document.put("tag", tag);
        document.put("totalkills", totalKills);
        document.put("totaldeaths", totalDeaths);

        Type staffProfileType = new TypeToken<StaffProfile>() {}.getType();
        document.put("staffProfile", OxygenBukkit.PLAIN_GSON.toJson(this.staffProfile, staffProfileType));

        Type commentsType = new TypeToken<List<Comment>>() {}.getType();
        document.put("comments", OxygenBukkit.PLAIN_GSON.toJson(this.comments, commentsType));
        Type messagesType = new TypeToken<List<Message>>() {}.getType();
        document.put("messages", OxygenBukkit.PLAIN_GSON.toJson(this.messages, messagesType));
        Type friendsType = new TypeToken<List<UUID>>() {}.getType();
        document.put("friends", OxygenBukkit.PLAIN_GSON.toJson(this.friends, friendsType));
        Type friendRequestsType = new TypeToken<List<UUID>>() {}.getType();
        document.put("friendrequests", OxygenBukkit.PLAIN_GSON.toJson(this.friendRequests, friendRequestsType));
        Type grantType = new TypeToken<List<Grant>>() {}.getType();
        document.put("grants", OxygenBukkit.PLAIN_GSON.toJson(this.grants, grantType));
        Type profilePermissionType = new TypeToken<List<ProfilePermission>>() {}.getType();
        document.put("profilePermissions", OxygenBukkit.PLAIN_GSON.toJson(this.profilePermissions, profilePermissionType));
        Type notesType = new TypeToken<List<Note>>() {}.getType();
        document.put("notes", OxygenBukkit.PLAIN_GSON.toJson(this.notes, notesType));
        Type statisticsType = new TypeToken<Map<StatisticsServer, Statistics>>() {}.getType();
        document.put("statistics", OxygenBukkit.PLAIN_GSON.toJson(this.statistics, statisticsType));
        Type socialMediaType = new TypeToken<Map<SocialMediaType, String>>() {}.getType();
        document.put("socialmedia", OxygenBukkit.PLAIN_GSON.toJson(this.socialMedia, socialMediaType));
        Type siblingsType = new TypeToken<List<Sibling>>() {}.getType();
        document.put("siblings", OxygenBukkit.PLAIN_GSON.toJson(this.siblings, siblingsType));

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
            addresses = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("addresses"), GSONUtil.LIST_STRING_TYPE);
            firstSeen = document.getLong("firstSeen");
            lastSeen = document.getLong("lastSeen");
            lastServer = document.getString("lastServer");
            tag = document.getString("tag");
            vpnWhitelist = document.getBoolean("vpnWhitelist");

            if (document.containsKey("staffProfile")) {
                Type staffProfileType = new TypeToken<StaffProfile>() {}.getType();
                staffProfile = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("staffProfile"), staffProfileType);
            }

            Type grantType = new TypeToken<List<Grant>>() {}.getType();
            grants = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("grants"), grantType);
            grants.removeIf(Objects::isNull);

            Type profilePermissionType = new TypeToken<List<ProfilePermission>>() {}.getType();
            profilePermissions = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("profilePermissions"), profilePermissionType);

            if (document.containsKey("notes")) {
                Type notesType = new TypeToken<List<Note>>() {}.getType();
                notes = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("notes"), notesType);
            }
            if (document.containsKey("siblings")) {
                Type siblingsType = new TypeToken<List<Sibling>>() {}.getType();
                siblings = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("siblings"), siblingsType);
            }

            Type commentsType = new TypeToken<List<Comment>>() {}.getType();
            comments = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("comments"), commentsType);
            Type messagesType = new TypeToken<List<Message>>() {}.getType();
            messages = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("messages"), messagesType);
            Type friendsType = new TypeToken<List<UUID>>() {}.getType();
            friends = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("friends"), friendsType);
            Type friendRequestType = new TypeToken<List<UUID>>() {}.getType();
            friendRequests = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("friendrequests"), friendRequestType);
            Type statisticsType = new TypeToken<Map<StatisticsServer, Statistics>>() {}.getType();
            statistics = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("statistics"), statisticsType);
            Type socialMediaType = new TypeToken<Map<SocialMediaType, String>>() {}.getType();
            socialMedia = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("socialmedia"), socialMediaType);


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

    public void calculatePlayerStatistics() {
        int kills = 0;
        int deaths = 0;

        for (Statistics value : this.statistics.values()) {
            kills += value.getKills();
            deaths += value.getDeaths();
        }

        this.totalKills = kills;
        this.totalDeaths = deaths;

        this.save();
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

        Rank rank = this.getActiveHighestRank();

        String displayName = CC.translate(rank.getPrefix() + rank.getDisplay_color().toString() + name + rank.getSuffix());
        if (!player.getDisplayName().equals(displayName + ChatColor.RESET)) {
            player.setDisplayName(displayName + ChatColor.RESET);
        }

        player.recalculatePermissions();

        plugin.getRedisModule().sendPacket(new PlayerPermissionPacket(player.getUniqueId(), bungeePermissions));
    }

    @NonNull
    public Rank getActiveHighestRank() {
        Rank defaultRank = plugin.getRankManager().getDefaultRank();

        if (defaultRank == null) {
            defaultRank = new Rank("Default");
            defaultRank.setDefaultRank(true);
        }

        return this.getActiveGrants().stream().filter(
                grant -> grant.getRank() != null && grant.getRank().getType() != RankType.HIDDEN)
                .map(Grant::getRank)
                .max(Comparator.comparingInt(Rank::getWeight))
                .orElse(defaultRank);
    }

    public Grant getHighestGrant() {
        return this.getActiveGrants().stream().filter(grant ->
                grant.getRank() != null && grant.getRank().getType() != RankType.HIDDEN)
                .max(Comparator.comparingInt(grant -> grant.getRank().getWeight()))
                .orElse(null);
    }

    public List<Grant> getActiveGrants() {
        return new ArrayList<>(this.grants).stream().filter(grant -> grant.isActive() && plugin.getRankManager().getRank(grant.getRankName()) != null).collect(Collectors.toList());
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
