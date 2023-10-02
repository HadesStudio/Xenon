package dev.hely.core.bukkit.profile;


import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.note.Note;
import dev.hely.core.bukkit.profile.notifications.Notification;
import dev.hely.core.bukkit.profile.permission.ProfilePermission;
import dev.hely.core.bukkit.profile.permission.packet.PlayerPermissionPacket;
import dev.hely.core.bukkit.profile.profilesystem.impl.Comment;
import dev.hely.core.bukkit.profile.profilesystem.impl.Message;
import dev.hely.core.bukkit.profile.profilesystem.impl.NotificationType;
import dev.hely.core.bukkit.profile.profilesystem.impl.Preferences;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.Statistics;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.StatisticsServer;
import dev.hely.core.bukkit.profile.profilesystem.impl.social.SocialMediaType;
import dev.hely.core.bukkit.profile.settings.ProfileSettings;
import dev.hely.core.bukkit.profile.siblings.Sibling;
import dev.hely.core.bukkit.profile.staff.StaffProfile;
import dev.hely.core.bukkit.profile.utils.PlaytimeUtil;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.punishment.PunishmentType;
import dev.hely.core.bukkit.ranks.gson.GSONUtil;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.events.GrantExpiredEvent;
import dev.hely.core.bukkit.ranks.newgrants.events.GrantGivenEvent;
import dev.hely.core.bukkit.ranks.newgrants.packets.GrantUpdatePacket;
import dev.hely.core.bukkit.ranks.newgrants.serializer.GrantSerializer;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
import dev.hely.core.bukkit.ranks.newgrants.types.RankGrant;
import dev.hely.core.bukkit.ranks.newgrants.types.TagGrant;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.ranks.rank.RankManager;
import dev.hely.core.bukkit.ranks.rank.RankType;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.core.bukkit.tag.TagManager;
import dev.hely.core.bukkit.utils.TextUtil;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name OxygenGlobal
 */
@Getter
@Setter
public class Profile {
    private OxygenBukkit plugin = OxygenBukkit.getInstance();

    private final UUID uniqueId;
    private String name;
    private boolean loaded = false;

    private String currentAddress = "";
    private List<String> addresses = new ArrayList<>();
    private List<UUID> alts = new ArrayList<>();
    private Long firstSeen;
    private Long lastSeen;
    private String currentServer = "None";
    private String lastServer = "";
    private String time = "";
    private int totalKills;
    private int totalDeaths;
    private List<Punishment> punishments = new ArrayList<>();
    private List<ProfilePermission> profilePermissions = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private List<String> playerPermissions = new ArrayList<>(); // RANK PERMISSIONS
    private List<String> bungeePermissions = new ArrayList<>(); // RANK BUNGEE PERMISSIONS
    //For notifications / mail system / profile system:
    private List<Notification> notifications = new ArrayList<>(); // String = Notification ID puta
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
    private int currency;
    private boolean frozen;
    private long playTime = 0;
    private PlaytimeUtil session;
    private List<String> ignoredList = new ArrayList<>();
    private long lastReceivedMessageMillis;
    private long lastSentMessageMillis;
    private String lastRepliedTo = null;
    private ProfileSettings settings = new ProfileSettings();
    private boolean needsVerify;
    private String authPassword = "";
    private long nextAuthenticationNeeded = -1, loginTime = -1;
    private boolean settingUpAuth = false;
    private String tfaKey = "";
    private List<Sibling> siblings = new ArrayList<>();
    private boolean discordSynced;
    private String discordID;
    private String discordName;
    private String vipColor = "YELLOW";
    private long vipCooldown = -1;

    // New Grant System:
    private RankGrant activeRankGrant;
    private List<Grant> grants = Lists.newArrayList();

    public Profile(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        this.totalKills = 0;
        this.totalDeaths = 0;
        this.currency = 0;

        for (StatisticsServer value : StatisticsServer.values()) {
            statistics.put(value, new Statistics(value));
        }

        this.preferences = new Preferences();
        this.session = new PlaytimeUtil(name + "-playtime-session");

        load();
    }

    public void save() {
        save(false);
    }

    public void save(boolean leave) {
        Document document = new Document();

        document.put("uniqueId", uniqueId.toString());
        document.put("name", name);
        document.put("authPassword", authPassword);
        document.put("needsVerify", needsVerify);
        document.put("nextAuthenticationNeeded", nextAuthenticationNeeded);
        document.put("loginTime", loginTime);
        document.put("settingUpAuth", settingUpAuth);
        document.put("tfaKey", tfaKey);
        document.put("playTime", playTime + session.calculateDifference());
        document.put("currentAddress", currentAddress);
        document.put("addresses", OxygenBukkit.PLAIN_GSON.toJson(addresses, GSONUtil.LIST_STRING_TYPE));
        document.put("firstSeen", firstSeen);
        document.put("lastSeen", lastSeen);
        document.put("lastServer", lastServer);
        document.put("time", time);
        document.put("vpnWhitelist", vpnWhitelist);
        document.put("tag", tag);
        document.put("totalkills", totalKills);
        document.put("frozen", frozen);
        document.put("totaldeaths", totalDeaths);
        document.put("currency", currency);
        document.put("lastReceivedMessageMillis", lastReceivedMessageMillis);
        document.put("lastSentMessageMillis", lastSentMessageMillis);
        document.put("lastRepliedTo", lastRepliedTo);
        document.put("vipColor", vipColor);
        document.put("vipCooldown", vipCooldown);

        document.put("discordID", discordID);
        document.put("discordSynced", discordSynced);
        document.put("discordName", discordName);


        Type settingsType = new TypeToken<ProfileSettings>() {}.getType();
        document.put("settings", OxygenBukkit.PLAIN_GSON.toJson(this.settings, settingsType));
        Type notificationsType = new TypeToken<List<Notification>>() {}.getType();
        document.put("notifications", OxygenBukkit.PLAIN_GSON.toJson(this.notifications, notificationsType));
        Type staffProfileType = new TypeToken<StaffProfile>() {}.getType();
        document.put("staffProfile", OxygenBukkit.PLAIN_GSON.toJson(this.staffProfile, staffProfileType));
        Type ignoredListType = new TypeToken<List<String>>() {}.getType();
        document.put("ignoredList", OxygenBukkit.PLAIN_GSON.toJson(this.comments, ignoredListType));
        Type commentsType = new TypeToken<List<Comment>>() {}.getType();
        document.put("comments", OxygenBukkit.PLAIN_GSON.toJson(this.comments, commentsType));
        Type messagesType = new TypeToken<List<Message>>() {}.getType();
        document.put("messages", OxygenBukkit.PLAIN_GSON.toJson(this.messages, messagesType));
        Type friendsType = new TypeToken<List<UUID>>() {}.getType();
        document.put("friends", OxygenBukkit.PLAIN_GSON.toJson(this.friends, friendsType));
        Type friendRequestsType = new TypeToken<List<UUID>>() {}.getType();
        document.put("friendrequests", OxygenBukkit.PLAIN_GSON.toJson(this.friendRequests, friendRequestsType));
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

        List<Document> grantDocuments = Lists.newArrayList();
        grants.stream().forEach(grant -> grantDocuments.add(GrantSerializer.INSTANCE.toDocument(grant)));
        document.put("grants", grantDocuments);

        plugin.getMongoModule().getProfilesCollection().replaceOne(Filters.eq("uniqueId", uniqueId.toString()), document, new ReplaceOptions().upsert(true));
        if (leave) plugin.getProfileManager().getProfileMap().remove(uniqueId);
    }

    public void load() {
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
            time = document.getString("time");
            tag = document.getString("tag");
            vpnWhitelist = document.getBoolean("vpnWhitelist");
            currency = document.getInteger("currency");
            playTime = document.getLong("playTime");
            if (document.getString("vipColor") == null) {
                vipColor = "YELLOW";
            } else {
                vipColor = document.getString("vipColor");
            }
            if (document.getLong("vipCooldown") == null) {
                vipCooldown = 0;
            } else {
                vipCooldown = document.getLong("vipCooldown");
            }
            discordSynced = document.getBoolean("discordSynced");
            discordID = document.getString("discordID");
            discordName = document.getString("discordName");
            Type notificationsType = new TypeToken<List<Notification>>() {}.getType();
            notifications = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("notifications"), notificationsType);
            authPassword = document.getString("authPassword");
            frozen = document.getBoolean("frozen");
            needsVerify = document.getBoolean("needsVerify");
            nextAuthenticationNeeded = document.getLong("nextAuthenticationNeeded");
            loginTime = document.getLong("loginTime");
            settingUpAuth = document.getBoolean("settingUpAuth");
            tfaKey = document.getString("tfaKey");
            lastReceivedMessageMillis = document.getLong("lastReceivedMessageMillis");
            lastSentMessageMillis = document.getLong("lastSentMessageMillis");
            lastRepliedTo = document.getString("lastRepliedTo");
            if (document.containsKey("settings")) {
                Type settingsType = new TypeToken<ProfileSettings>() {}.getType();
                settings = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("settings"), settingsType);
            }
            if (document.containsKey("staffProfile")) {
                Type staffProfileType = new TypeToken<StaffProfile>() {}.getType();
                staffProfile = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("staffProfile"), staffProfileType);
            }
            Type ignoreListType = new TypeToken<List<String>>() {}.getType();
            ignoredList = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("ignoredList"), ignoreListType);
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
            Type ignoredListType = new TypeToken<List<String>>() {}.getType();
            ignoredList = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("ignoredList"), ignoredListType);
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


            List<Document> grantDocuments = document.getList("grants", Document.class);
            grants.clear();
            grantDocuments.stream().forEach(it -> grants.add(GrantSerializer.INSTANCE.fromDocument(it)));

            try (MongoCursor<Document> mongoCursor = plugin.getMongoModule().getPunishmentsCollection().find(Filters.eq("targetUUID", uniqueId.toString())).iterator()) {
                mongoCursor.forEachRemaining(punishmentDocument -> {
                    Punishment punishment = new Punishment();

                    punishment.load(punishmentDocument);
                    punishments.add(punishment);
                });
            }

            activateNextRankGrant();
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

    public boolean canInteact(Profile profile) {
        if (profile == null) {
            return false;
        }
        int weight = this.getActiveRank().getWeight();
        int otherWeight = profile.getActiveRank().getWeight();

        return otherWeight < weight;
    }


    public Punishment getActivePunishmentByType(PunishmentType type) {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == type && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }

    public String getTagFormated() {
        if (this.getTag() == null || this.getTag().isEmpty()) {
            return "";
        } else {
            Tag tag = OxygenBukkit.getInstance().getTagManager().getTag(this.getTag());

            return tag.getPrefix();
        }
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

    public long getTotalPlayTime() {
        return playTime + session.calculateDifference();
    }

    public int getQueuePriority() {
        int queuePriority = this.getActiveRank().getQueuePriority();

        return (queuePriority == 0 ? 0 : this.getActiveRank().getQueuePriority() + (hasVIPStatus() ? OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.settings.queue-priority-amount") : 0));
    }

    public boolean hasVIPStatus() {
        for (RankGrant grant : this.getRankGrants()) {
            if (grant.getRankName().equalsIgnoreCase(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.settings.grant-name"))) {
                return true;
            }
        }
        return false;
    }

    public void setupAlts() {
        alts.clear();

        try (MongoCursor<Document> cursor = plugin.getMongoModule().getProfilesCollection().find(Filters.eq("addresses", addresses)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                Profile profile = new Profile(UUID.fromString(document.getString("uniqueId")), document.getString("name"));

                if (!profile.getUniqueId().toString().equals(this.uniqueId.toString())) {
                    this.alts.add(profile.getUniqueId());
                }
            }
        }
    }


    // New Grants System:

    public void setupPermissionAttachments(Player player) {
        try {
            Set<PermissionAttachmentInfo> attachments = player.getEffectivePermissions();
            for (PermissionAttachmentInfo attachmentInfo : attachments) {
                if (attachmentInfo.getAttachment() == null) continue;

                Iterator<String> permissions = attachmentInfo.getAttachment().getPermissions().keySet().iterator();

                while (permissions.hasNext()) {
                    String permission = permissions.next();
                    attachmentInfo.getAttachment().unsetPermission(permission);
                }
            }

        } catch (Exception e) { }

        PermissionAttachment attachment = player.addAttachment(OxygenBukkit.getInstance());
        if (attachment == null) return;

        List<String> bungeePermissions = Lists.newArrayList();

        attachment.getPermissions().keySet().forEach(attachment::unsetPermission);

        // setup active rank permissions
        RankManager rankManager = OxygenBukkit.getInstance().getRankManager();
        if (activeRankGrant != null && rankManager.getRank(activeRankGrant.getRankName()) != null) {
            Rank activeRank = rankManager.getRank(activeRankGrant.getRankName());

            activeRank.getPermissionsData().stream().filter(perm -> !TextUtil.isNull(perm.getPermission()))
                    .forEach(perm -> {
                        if (perm.isGlobal()) {
                            bungeePermissions.add(perm.getPermission());
                        } else {
                            attachment.setPermission(perm.getPermission(), true);
                        }
                    });
        }

        // setup player permissions
        getPermissionGrants().stream().filter(grant -> !grant.isRemoved()).filter(grant -> !grant.hasExpired())
                .filter(Grant::canActivate).forEach(perm -> {
                    if (perm.isBungeecord()) {
                        bungeePermissions.add(perm.getPermission());
                    } else {
                        attachment.setPermission(perm.getPermission(), true);
                    }
                });

        player.recalculatePermissions();
        player.setDisplayName(getColoredName() + ChatColor.RESET);

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new PlayerPermissionPacket(player.getUniqueId(), bungeePermissions));
    }

    public boolean hasPermission(String permission) {
        if (Bukkit.getPlayer(this.getUniqueId()) != null) {
            if (Bukkit.getPlayer(this.getUniqueId()).hasPermission(permission)) {
                return true;
            }
        }

        if (permission.equalsIgnoreCase("*") || permission.equalsIgnoreCase("'*'")) {
            return true;
        }

        for (PermissionGrant permissionGrant : getPermissionGrants()) {
            if (permissionGrant.getPermission().equalsIgnoreCase(permission)) {
                if (!permissionGrant.isRemoved() && !permissionGrant.hasExpired() && permissionGrant.canActivate()) {
                    return true;
                }
            }
        }

        Rank rank = getActiveRank();

        return rank.hasPermission(permission);
    }

    public Rank getActiveRank() {
        RankManager rankManager = OxygenBukkit.getInstance().getRankManager();

        if (activeRankGrant != null) {
            return rankManager.getRank(activeRankGrant.getRankName());
        }

        return rankManager.getDefaultRank();
    }

    public void addGrant(Grant grant) {
        if (!grants.contains(grant)) {
            grants.add(grant);

            if (Bukkit.getPlayer(this.getUniqueId()) != null && Bukkit.getPlayer(this.getUniqueId()).isOnline()) {
                new GrantGivenEvent(Bukkit.getPlayer(this.getUniqueId()), grant);

                activateNextRankGrant();
            }

            this.save();

            OxygenBukkit.getInstance().getRedisModule().sendPacket(new GrantUpdatePacket(this.getUniqueId()));
        }
    }

    public void updateGrants() {
        RankManager rankManager = OxygenBukkit.getInstance().getRankManager();

        for (Grant grant : grants) {
            if (grant.hasExpired() && !grant.isRemoved()) {
                grant.setRemoved(true);
                grant.setRemovedReason("Expired Grant.");
                grant.setRemovedAt(System.currentTimeMillis());

                if (Bukkit.getPlayer(this.getUniqueId()) != null && Bukkit.getPlayer(this.getUniqueId()).isOnline()) {
                    new GrantExpiredEvent(Bukkit.getPlayer(this.getUniqueId()), grant);
                }

                if (grant instanceof RankGrant) {
                    RankGrant rankGrant = (RankGrant) grant;

                    if (rankManager.getRank(rankGrant.getRankName()) == null) {
                        setActiveRankGrant(null);
                    }

                    if (grant.equals(activeRankGrant)) {
                        setActiveRankGrant(null);
                    }
                }
            }
        }

        if (activeRankGrant == null) {
            activateNextRankGrant();

            if (activeRankGrant == null) {
                RankGrant defaultGrant = new RankGrant(UUID.randomUUID(), rankManager.getDefaultRank().getName());
                defaultGrant.setAddedReason("Grant expiration.");
                defaultGrant.setAddedAt(System.currentTimeMillis());
                defaultGrant.setDuration(Integer.MAX_VALUE);

                addGrant(defaultGrant);
                setActiveRankGrant(defaultGrant);

                if (Bukkit.getPlayer(this.getUniqueId()) != null && Bukkit.getPlayer(this.getUniqueId()).isOnline()) {
                    setupPermissionAttachments(Bukkit.getPlayer(this.getUniqueId()));
                }
            }
        }
    }

    public void activateNextRankGrant() {
        RankManager rankManager = OxygenBukkit.getInstance().getRankManager();
        List<RankGrant> rankGrants = getRankGrants();

        rankGrants.sort(Comparator.comparingInt(rankGrant -> rankManager.getRank(rankGrant.getRankName()).getWeight()));

        Collections.reverse(rankGrants);

        for (RankGrant rankGrant : rankGrants) {
            if (!rankGrant.hasExpired() && !rankGrant.isRemoved()) {

                if (rankGrant.canActivate()) {

                    Rank rank = rankManager.getRank(rankGrant.getRankName());
                    if(rank != null) {
                        setActiveRankGrant(rankGrant);

                        if (Bukkit.getPlayer(this.getUniqueId()) != null && Bukkit.getPlayer(this.getUniqueId()).isOnline()) {
                            setupPermissionAttachments(Bukkit.getPlayer(this.getUniqueId()));
                        }

                        break;
                    }
                }
            }
        }
    }


    public List<RankGrant> getRankGrants() {
        List<RankGrant> rankGrants = new ArrayList<>();

        RankManager rankManager = OxygenBukkit.getInstance().getRankManager();
        
        grants.stream().filter(grant -> grant instanceof RankGrant)
                .filter(grant -> rankManager.getRank(((RankGrant) grant).getRankName()) != null)
                .forEach(grant -> rankGrants.add((RankGrant) grant));

        return rankGrants;
    }

    public List<PermissionGrant> getPermissionGrants() {
        List<PermissionGrant> permissionGrants = new ArrayList<>();

        grants.stream().filter(grant -> grant instanceof PermissionGrant)
                .forEach(grant -> permissionGrants.add((PermissionGrant) grant));

        return permissionGrants;
    }

    public List<TagGrant> getTagGrants() {
        List<TagGrant> tagGrants = new ArrayList<>();

        TagManager tagManager = OxygenBukkit.getInstance().getTagManager();

        grants.stream().filter(grant -> grant instanceof TagGrant)
                .filter(grant -> tagManager.getTag(((TagGrant) grant).getTagName()) != null)
                .forEach(grant -> tagGrants.add((TagGrant) grant));

        return tagGrants;
    }

    public String getColoredName() {
        Rank rank = getActiveRank();
        return (rank == null ? "" : rank.getDisplay_color()) + name;
    }
}
