package net.ascend.core.bukkit.profile;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.note.Note;
import net.ascend.core.bukkit.profile.notifications.Notification;
import net.ascend.core.bukkit.profile.permission.ProfilePermission;
import net.ascend.core.bukkit.profile.permission.packet.PlayerPermissionPacket;
import net.ascend.core.bukkit.profile.myprofile.SocialMediaType;
import net.ascend.core.bukkit.profile.settings.ProfileSettings;
import net.ascend.core.bukkit.profile.staff.StaffProfile;
import net.ascend.core.bukkit.profile.playtime.PlaytimeUtil;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.rank.gson.GSONUtil;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.event.GrantExpiredEvent;
import net.ascend.core.bukkit.rank.grant.event.GrantGivenEvent;
import net.ascend.core.bukkit.rank.grant.packet.impl.GrantUpdatePacket;
import net.ascend.core.bukkit.rank.grant.serializer.GrantSerializer;
import net.ascend.core.bukkit.rank.grant.type.RankGrant;
import net.ascend.core.bukkit.rank.grant.type.TagGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagManager;
import net.ascend.core.bukkit.util.TextUtil;
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
 * Protect Name Xenon
 */
@Getter
@Setter
public class Profile {

    private XenonBukkit plugin = XenonBukkit.getInstance();

    private final UUID uniqueId;
    private String name;
    private boolean loaded = false;

    private String currentAddress = "";
    private List<String> addresses = new ArrayList<>();
    private List<UUID> alts = new ArrayList<>();
    private Long firstSeen = 0L;
    private Long lastSeen = 0L;
    private String currentServer = "None";
    private String lastServer = "";

    private String time = "";

    private List<Punishment> punishments = new ArrayList<>();
    private List<ProfilePermission> profilePermissions = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();

    private List<String> playerPermissions = new ArrayList<>(); // RANK PERMISSIONS
    private List<String> bungeePermissions = new ArrayList<>(); // RANK BUNGEE PERMISSIONS

    private List<Notification> notifications = new ArrayList<>();

    private List<UUID> friends = new ArrayList<>();
    private List<UUID> friendRequests = new ArrayList<>();

    private Map<SocialMediaType, String> socialMedia = new HashMap<>();

    private int currency = 0;

    private boolean frozen;

    private long playTime = 0;
    private PlaytimeUtil session;

    private List<String> ignoredList = new ArrayList<>();
    private long lastReceivedMessageMillis;
    private long lastSentMessageMillis;
    private String lastRepliedTo = null;

    private StaffProfile staffProfile = new StaffProfile();
    private ProfileSettings settings = new ProfileSettings();

    private String tag = "";

    private RankGrant activeRankGrant;
    private List<Grant> grants = Lists.newArrayList();

    public Profile(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;

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

        document.put("currentAddress", currentAddress);
        document.put("addresses", XenonBukkit.PLAIN_GSON.toJson(addresses, GSONUtil.LIST_STRING_TYPE));

        document.put("firstSeen", firstSeen);
        document.put("lastSeen", lastSeen);
        document.put("lastServer", lastServer);


        document.put("frozen", frozen);
        document.put("currency", currency);

        document.put("time", time);
        document.put("playTime", playTime + session.calculateDifference());

        // PRIVATE MESSAGES
        document.put("lastReceivedMessageMillis", lastReceivedMessageMillis);
        document.put("lastSentMessageMillis", lastSentMessageMillis);
        document.put("lastRepliedTo", lastRepliedTo);


        Type profilePermissionType = new TypeToken<List<ProfilePermission>>() {}.getType();
        document.put("profilePermissions", XenonBukkit.PLAIN_GSON.toJson(this.profilePermissions, profilePermissionType));

        Type staffProfileType = new TypeToken<StaffProfile>() {}.getType();
        document.put("staffProfile", XenonBukkit.PLAIN_GSON.toJson(this.staffProfile, staffProfileType));

        Type notesType = new TypeToken<List<Note>>() {}.getType();
        document.put("notes", XenonBukkit.PLAIN_GSON.toJson(this.notes, notesType));

        Type notificationsType = new TypeToken<List<Notification>>() {}.getType();
        document.put("notifications", XenonBukkit.PLAIN_GSON.toJson(this.notifications, notificationsType));

        Type socialMediaType = new TypeToken<Map<SocialMediaType, String>>() {}.getType();
        document.put("socialmedia", XenonBukkit.PLAIN_GSON.toJson(this.socialMedia, socialMediaType));

        Type friendsType = new TypeToken<List<UUID>>() {}.getType();
        document.put("friends", XenonBukkit.PLAIN_GSON.toJson(this.friends, friendsType));
        Type friendRequestsType = new TypeToken<List<UUID>>() {}.getType();
        document.put("friendrequests", XenonBukkit.PLAIN_GSON.toJson(this.friendRequests, friendRequestsType));


        Type settingsType = new TypeToken<ProfileSettings>() {}.getType();
        document.put("settings", XenonBukkit.PLAIN_GSON.toJson(this.settings, settingsType));

        Type ignoredListType = new TypeToken<List<String>>() {}.getType();
        document.put("ignoredList", XenonBukkit.PLAIN_GSON.toJson(this.ignoredList, ignoredListType));


        document.put("tag", tag);

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
            addresses = XenonBukkit.PLAIN_GSON.fromJson(document.getString("addresses"), GSONUtil.LIST_STRING_TYPE);
            firstSeen = document.getLong("firstSeen");
            lastSeen = document.getLong("lastSeen");
            lastServer = document.getString("lastServer");

            tag = document.getString("tag");

            time = document.getString("time");
            currency = document.getInteger("currency");
            playTime = document.getLong("playTime");


            Type notificationsType = new TypeToken<List<Notification>>() {}.getType();
            notifications = XenonBukkit.PLAIN_GSON.fromJson(document.getString("notifications"), notificationsType);

            frozen = document.getBoolean("frozen");

            // PRIVATE MESSAGES
            lastReceivedMessageMillis = document.getLong("lastReceivedMessageMillis");
            lastSentMessageMillis = document.getLong("lastSentMessageMillis");
            lastRepliedTo = document.getString("lastRepliedTo");

            List<Document> grantDocuments = document.getList("grants", Document.class);
            grants.clear();
            grantDocuments.stream().forEach(it -> grants.add(GrantSerializer.INSTANCE.fromDocument(it)));

            Type profilePermissionType = new TypeToken<List<ProfilePermission>>() {}.getType();
            profilePermissions = XenonBukkit.PLAIN_GSON.fromJson(document.getString("profilePermissions"), profilePermissionType);

            if (document.containsKey("notes")) {
                Type notesType = new TypeToken<List<Note>>() {}.getType();
                notes = XenonBukkit.PLAIN_GSON.fromJson(document.getString("notes"), notesType);
            }

            Type friendsType = new TypeToken<List<UUID>>() {}.getType();
            friends = XenonBukkit.PLAIN_GSON.fromJson(document.getString("friends"), friendsType);
            Type friendRequestType = new TypeToken<List<UUID>>() {}.getType();
            friendRequests = XenonBukkit.PLAIN_GSON.fromJson(document.getString("friendrequests"), friendRequestType);

            Type socialMediaType = new TypeToken<Map<SocialMediaType, String>>() {}.getType();
            socialMedia = XenonBukkit.PLAIN_GSON.fromJson(document.getString("socialmedia"), socialMediaType);

            Type ignoredListType = new TypeToken<List<String>>() {}.getType();
            ignoredList = XenonBukkit.PLAIN_GSON.fromJson(document.getString("ignoredList"), ignoredListType);

            if (document.containsKey("settings")) {
                Type settingsType = new TypeToken<ProfileSettings>() {}.getType();
                settings = XenonBukkit.PLAIN_GSON.fromJson(document.getString("settings"), settingsType);
            }
            if (document.containsKey("staffProfile")) {
                Type staffProfileType = new TypeToken<StaffProfile>() {}.getType();
                staffProfile = XenonBukkit.PLAIN_GSON.fromJson(document.getString("staffProfile"), staffProfileType);
            }

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

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
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

    public boolean canSelectTag(Tag tag) {
        Player player = getPlayer();
        if (player != null && player.isOnline()) {
            if (player.hasPermission(tag.getPermission())) {
                return true;
            }
        }

        for (TagGrant tagGrant : getTagGrants()) {
            if (!tagGrant.hasExpired()) {
                if (!tagGrant.isRemoved()) {
                    if (tagGrant.canActivate()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Tag getActiveTag() {
        if (this.getTag() == null || this.getTag().isEmpty()) {
            return null;
        }

        Tag tag = XenonBukkit.getInstance().getTagManager().getTag(this.getTag());
        if (tag != null) {
            if (canSelectTag(tag)) {
                return tag;
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

    public long getTotalPlayTime() {
        return playTime + session.calculateDifference();
    }

    public void setupAlts() {
        this.alts.clear();

        try (MongoCursor<Document> cursor = plugin.getMongoModule().getProfilesCollection().find(Filters.eq("currentAddress", currentAddress)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                Profile profile = new Profile(UUID.fromString(document.getString("uniqueId")), document.getString("name"));

                if (!profile.getUniqueId().toString().equals(this.uniqueId.toString())) {
                    this.alts.add(profile.getUniqueId());
                }
            }
        }
    }

    public void setupPermissionAttachments(Player player) {
        this.playerPermissions.clear();
        this.bungeePermissions.clear();

        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null) {
                continue;
            }
            if (attachmentInfo.getAttachment() == null) continue;
            if (attachmentInfo.getAttachment().getPlugin() == null) continue;
            if (attachmentInfo.getAttachment().getPlugin() != XenonBukkit.getInstance()) continue;


            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
                attachmentInfo.getAttachment().unsetPermission(permission);
            });
        }

        PermissionAttachment attachment = player.addAttachment(XenonBukkit.getInstance());
        if (attachment == null) return;
        attachment.getPermissions().keySet().forEach(attachment::unsetPermission);

        RankManager rankManager = XenonBukkit.getInstance().getRankManager();
        if (activeRankGrant != null && rankManager.getRank(activeRankGrant.getRankName()) != null) {
            Rank activeRank = rankManager.getRank(activeRankGrant.getRankName());
            if (activeRank.isOnServer()) {

                activeRank.getPermissionsData().stream()
                        .filter(perm -> perm.isAvailable() && !TextUtil.isNull(perm.getPermission()))
                        .forEach(perm -> {
                            if (perm.isBungeecord()) {
                                this.bungeePermissions.add(perm.getPermission());
                            } else {
                                this.playerPermissions.add(perm.getPermission());
                            }
                        });


                List<String> inheritances = new ArrayList<>(activeRank.getInheritances());
                inheritances.forEach(inheritance -> {
                    Rank rankInheritance = rankManager.getRank(inheritance);

                    if (rankInheritance != null && rankInheritance.isOnServer()) {
                        rankInheritance.getPermissionsData().stream()
                                .filter(perm -> perm.isAvailable() && !TextUtil.isNull(perm.getPermission()))
                                .forEach(perm -> {
                                    if (perm.isBungeecord()) {
                                        this.bungeePermissions.add(perm.getPermission());
                                    } else {
                                        this.playerPermissions.add(perm.getPermission());
                                    }
                                });
                    }
                });
            }
        }

        Rank defaultRank = rankManager.getDefaultRank();
        if (defaultRank != null && defaultRank.isOnServer()) {
            defaultRank.getPermissionsData().stream()
                    .filter(perm -> perm.isAvailable() && !TextUtil.isNull(perm.getPermission()))
                    .forEach(perm -> {
                        if (perm.isBungeecord()) {
                            this.bungeePermissions.add(perm.getPermission());
                        } else {
                            this.playerPermissions.add(perm.getPermission());
                        }
                    });



            List<String> inheritances = new ArrayList<>(defaultRank.getInheritances());
            inheritances.forEach(inheritance -> {
                Rank rankInheritance = rankManager.getRank(inheritance);

                if (rankInheritance != null && rankInheritance.isOnServer()) {
                    rankInheritance.getPermissionsData().stream()
                            .filter(perm -> perm.isAvailable() && !TextUtil.isNull(perm.getPermission()))
                            .forEach(perm -> {
                                if (perm.isBungeecord()) {
                                    this.bungeePermissions.add(perm.getPermission());
                                } else {
                                    this.playerPermissions.add(perm.getPermission());
                                }
                            });
                }
            });
        }

        this.profilePermissions.removeIf(ProfilePermission::isExpired);

        List<ProfilePermission> playerPermissions = new ArrayList<>(this.profilePermissions);
        playerPermissions.forEach(permission -> {
            if (permission.isActive()) {
                this.playerPermissions.add(permission.getPermission());
            }
        });

        this.playerPermissions.forEach(playerPermission -> attachment.setPermission(playerPermission, true)); // FINISHED

        player.recalculatePermissions();
        player.setDisplayName(getColoredName() + ChatColor.RESET);

        XenonBukkit.getInstance().getRedisModule().sendPacket(new PlayerPermissionPacket(player.getUniqueId(), this.bungeePermissions));
    }

    public Rank getActiveRank() {
        RankManager rankManager = XenonBukkit.getInstance().getRankManager();

        if (activeRankGrant != null) {
            return rankManager.getRank(activeRankGrant.getRankName());
        }

        return rankManager.getDefaultRank();
    }

    public void addGrant(Grant grant) {
        if (!grants.contains(grant)) {
            grants.add(grant);

            Player player = getPlayer();
            if (player != null && player.isOnline()) {
                new GrantGivenEvent(player, grant);

                activateNextRankGrant();
            }

            this.save();

            XenonBukkit.getInstance().getRedisModule().sendPacket(new GrantUpdatePacket(this.getUniqueId()));
        }
    }

    public void updateGrants() {
        Player player = getPlayer();

        RankManager rankManager = XenonBukkit.getInstance().getRankManager();

        for (Grant grant : grants) {
            if (grant.hasExpired() && !grant.isRemoved()) {
                grant.setRemoved(true);
                grant.setRemovedReason("Expired Grant.");
                grant.setRemovedAt(System.currentTimeMillis());

                if (player != null && player.isOnline()) {
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
                defaultGrant.setAddedReason("Default Grant.");
                defaultGrant.setAddedAt(System.currentTimeMillis());
                defaultGrant.setDuration(Integer.MAX_VALUE);

                addGrant(defaultGrant);
                setActiveRankGrant(defaultGrant);

                if (player != null && player.isOnline()) {
                    setupPermissionAttachments(player);
                }
            }
        }
    }

    public void activateNextRankGrant() {
        Player player = getPlayer();

        RankManager rankManager = XenonBukkit.getInstance().getRankManager();
        List<RankGrant> rankGrants = getRankGrants();

        rankGrants.sort(Comparator.comparingInt(rankGrant -> rankManager.getRank(rankGrant.getRankName()).getWeight()));
        Collections.reverse(rankGrants);

        for (RankGrant rankGrant : rankGrants) {
            if (!rankGrant.hasExpired() && !rankGrant.isRemoved()) {
                if (rankGrant.canActivate()) {
                    Rank rank = rankManager.getRank(rankGrant.getRankName());
                    if(rank != null) {
                        setActiveRankGrant(rankGrant);

                        if (player != null && player.isOnline()) {
                            setupPermissionAttachments(player);
                        }
                        break;
                    }
                }
            }
        }
    }

    public List<RankGrant> getRankGrants() {
        List<RankGrant> rankGrants = new ArrayList<>();

        RankManager rankManager = XenonBukkit.getInstance().getRankManager();
        
        grants.stream().filter(grant -> grant instanceof RankGrant)
                .filter(grant -> rankManager.getRank(((RankGrant) grant).getRankName()) != null)
                .forEach(grant -> rankGrants.add((RankGrant) grant));

        return rankGrants;
    }

    public List<TagGrant> getTagGrants() {
        List<TagGrant> tagGrants = new ArrayList<>();

        TagManager tagManager = XenonBukkit.getInstance().getTagManager();

        grants.stream().filter(grant -> grant instanceof TagGrant)
                .filter(grant -> tagManager.getTag(((TagGrant) grant).getTagName()) != null)
                .forEach(grant -> tagGrants.add((TagGrant) grant));

        return tagGrants;
    }

    public String getColoredName() {
        Rank rank = getActiveRank();
        return (rank == null ? "" : rank.getDisplay_color()) + name;
    }

    public String getColor() {
        Rank rank = getActiveRank();
        return (rank == null ? "" : rank.getDisplay_color().toString());
    }
}
