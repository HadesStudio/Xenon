package dev.hely.core.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.database.redis.cache.CachedData;
import dev.hely.core.ranks.Ranks;
import dev.hely.core.ranks.grant.Grants;
import dev.hely.core.ranks.gson.RankGsonType;
import dev.hely.core.ranks.permissions.Permission;
import dev.hely.core.ranks.types.RankType;
import dev.hely.core.utils.PluginUtils;
import dev.hely.lib.CC;
import lombok.*;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

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

    private List<Grants> grants = new ArrayList<>();
    private List<Permission> permissions = new ArrayList<>();
    private List<String> bungeePermissions = new ArrayList<>();
    private List<String> attachmentPermissions = new ArrayList<>();

    private boolean staffChat = false, adminChat = false;

    private boolean loadingPermissions = false;
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
        document.put("staffChat", staffChat);
        document.put("adminChat", adminChat);
        document.put("grants", Oxygen.PLAIN_GSON.toJson(this.grants, RankGsonType.GRANT));
        document.put("permissions", Oxygen.PLAIN_GSON.toJson(this.permissions, RankGsonType.PERMISSION));
        document.put("bungeePermissions", Oxygen.PLAIN_GSON.toJson(this.bungeePermissions, RankGsonType.PERMISSION));

        plugin.getMongoManager().getProfilesCollection().replaceOne(Filters.eq("uniqueId", uniqueId.toString()), document, new ReplaceOptions().upsert(true));
        if (leave) plugin.getProfileManager().getProfileMap().remove(uniqueId);
    }

    public void loadAttachments(Player player) {
        this.loadAttachments(player, false);
    }

    public void loadAttachments(Player player, boolean login) {
        if (this.loadingPermissions) return;

        this.loadingPermissions = true;

        if (!login) {
            this.loadInPermissions(player);
        }

        this.sendProxyPermissionsCacheUpdate();

        Ranks rankData = this.getHighestRank();
        if (!player.getDisplayName().equals(CC.translate(rankData.getPrefix() + rankData.getColor().toString() + this.getName() + rankData.getSuffix()) + ChatColor.RESET)) {
            player.setDisplayName(CC.translate(rankData.getPrefix() + rankData.getColor().toString()+ this.getName() + rankData.getSuffix()) + ChatColor.RESET);
        }

        this.loadingPermissions = false;
    }

    public void loadInPermissions(Player player) {
        this.attachmentPermissions.clear();
        this.bungeePermissions.clear();

        List<Grants> currentGrants = new ArrayList<>(this.grants);
        for (Grants grant : currentGrants) {
            if (grant.hasExpired()) continue;

            Ranks rankData = plugin.getRankManager().getRank(grant.getRankName());

            if (rankData != null && rankData.isAvailableOnServer()) {
                List<String> rankPermissions = new ArrayList<>(rankData.getAvailablePermissions());
                this.attachmentPermissions.addAll(rankPermissions);
                if (rankData.isBungee()) {
                    bungeePermissions.addAll(rankData.getBungeePermissions());
                }

                List<String> inheritances = new ArrayList<>(rankData.getInheritance());
                inheritances.forEach(inheritance -> {
                    Ranks rankInheritance = plugin.getRankManager().getRank(inheritance);

                    if (rankInheritance != null && rankInheritance.isAvailableOnServer()) {
                        List<String> inheritancePermissions = new ArrayList<>(rankInheritance.getAvailablePermissions());
                        this.attachmentPermissions.addAll(inheritancePermissions);
                        if (rankInheritance.isBungee()) {
                            bungeePermissions.addAll(rankInheritance.getBungeePermissions());
                        }
                    }
                });
            }
        }

        Ranks defaultRank = plugin.getRankManager().getDefaultRank();
        if (defaultRank != null && defaultRank.isAvailableOnServer()) {
            List<String> defaultPermissions = new ArrayList<>(defaultRank.getAvailablePermissions());

            this.attachmentPermissions.addAll(defaultPermissions);
            if (defaultRank.isBungee()) {
                bungeePermissions.addAll(defaultRank.getBungeePermissions());
            }

            List<String> inheritances = new ArrayList<>(defaultRank.getInheritance());
            inheritances.forEach(inheritance -> {
                Ranks rankInheritance = plugin.getRankManager().getRank(inheritance);

                if (rankInheritance != null && rankInheritance.isAvailableOnServer()) {
                    List<String> inheritancePermissions = new ArrayList<>(rankInheritance.getAvailablePermissions());
                    this.attachmentPermissions.addAll(inheritancePermissions);
                    if (rankInheritance.isBungee()) {
                        bungeePermissions.addAll(rankInheritance.getBungeePermissions());
                    }
                }
            });
        }

        this.permissions.removeIf(Permission::isExpired);

        List<Permission> playerPermissions = new ArrayList<>(this.permissions);
        playerPermissions.forEach(permission -> {
            if (permission.isAvailable()) {
                this.attachmentPermissions.add(permission.getPermission());
            }
        });
    }

    public void load() {
        CachedData cachedData = new CachedData(this.plugin, this.uniqueId, this.name);
        Document data = cachedData.getData();
        this.load(data);
    }

    public void load(Document document) {
        if (document == null) {
            document = plugin.getMongoManager().getProfilesCollection().find(Filters.eq("uniqueId", this.uniqueId.toString())).first();
        }
        if (document == null) {
            return;
        }

        this.grants = Oxygen.PLAIN_GSON.fromJson(document.getString("grants"), RankGsonType.GRANT);
        this.grants.removeIf(Objects::isNull);

        this.permissions = Oxygen.PLAIN_GSON.fromJson(document.getString("permissions"), RankGsonType.PERMISSION);
        this.lastSeen = document.getLong("lastSeen");
        this.staffChat = document.getBoolean("staffChat");
        this.adminChat = document.getBoolean("adminChat");
        if (document.containsKey("bungeePermissions")) {
            this.bungeePermissions = Oxygen.PLAIN_GSON.fromJson(document.getString("bungeePermissions"), RankGsonType.STRING_LIST);
        }
        this.loaded = true;
    }

    @NonNull
    public Ranks getHighestRank() {
        Ranks defaultRank = plugin.getRankManager().getDefaultRank();

        if (defaultRank == null) {
            defaultRank = new Ranks(this.plugin, "Default", PluginUtils.randomString());
            defaultRank.setDefaultRank(true);
        }

        return this.getActiveGrants().stream().filter(grant -> grant.getRank() != null && grant.getRank().getRankType() != RankType.HIDDEN).map(Grants::getRank)
                .max(Comparator.comparingInt(Ranks::getWeight)).orElse(defaultRank);
    }

    public Grants getHighestGrant() {
        return this.getActiveGrants().stream().filter(grant -> grant.getRank() != null && grant.getRank().getRankType() != RankType.HIDDEN).max(Comparator.comparingInt(grant -> grant.getRank().getWeight())).orElse(null);
    }

    public List<Grants> getActiveGrants() {
        return new ArrayList<>(this.grants).stream().filter(grant -> !grant.hasExpired() && plugin.getRankManager().getRank(grant.getRankName()) != null).collect(Collectors.toList());
    }

    public void sendProxyPermissionsCacheUpdate() {
        JsonObject data = new JsonObject();

        data.addProperty("type", "proxy_perms_update");
        data.addProperty("uniqueId", this.uniqueId.toString());

        JsonArray array = new JsonArray();
        this.bungeePermissions.forEach(array::add);
        data.add("bungeePermissions", array);

        this.plugin.getRedisManager().sendProxyRequest(data);
    }

    public boolean isPermissionSet(String permission) {
        return this.permissions.stream().filter(perm -> perm.getPermission().equalsIgnoreCase(permission)).findFirst().orElse(null) != null;
    }
}
