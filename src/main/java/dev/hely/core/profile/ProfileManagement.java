package dev.hely.core.profile;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile
//   Date: Tuesday, July 04, 2023 - 6:03 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.client.model.Filters;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.database.redis.cache.CachedData;
import dev.hely.core.managers.server.global_player.GlobalPlayer;
import dev.hely.core.ranks.permissions.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class ProfileManagement {
    private final Oxygen plugin;

    private Map<UUID, Profile> playerData = new HashMap<>();
    private Map<UUID, Integer> quitTasksId = new HashMap<>();

    public Profile createPlayerData(UUID uuid, String name) {
        if (this.playerData.containsKey(uuid)) return getPlayerData(uuid);

        this.playerData.put(uuid, new Profile(uuid, name));
        return getPlayerData(uuid);
    }

    public Profile getPlayerData(UUID uuid) {
        return this.playerData.get(uuid);
    }

    public void deleteData(UUID uuid) {
        if (Bukkit.getPlayer(uuid) == null) {
            this.playerData.remove(uuid);
        }
    }

    public Profile getOfflineLoadedData(String name) {
        return this.getOfflineLoadedData(name, false);
    }

    public Profile getOfflineLoadedData(String name, boolean neverJoinedCheck) {
        Player player = Bukkit.getPlayer(name);

        if (player != null) {
            Profile data = this.plugin.getProfileManager().getProfile(player.getUniqueId());

            if (data != null) {
                return data;
            }
        }

        CachedData cachedData = new CachedData(this.plugin, null, name);
        Document document = cachedData.getData();

        if (document != null) {
            Profile playerData = new Profile(UUID.fromString(document.getString("uuid")), document.getString("name"));
            playerData.load(document);
            return playerData;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

        document = plugin.getMongoManager().getProfilesCollection().find(Filters.or(Filters.eq("uniqueId", offlinePlayer.getUniqueId().toString()),
                Filters.eq("name", name.toLowerCase()))).first();

        if (document == null) {
            if (neverJoinedCheck) {
                return null;
            }
            return new Profile(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        }

        Profile playerData = new Profile(UUID.fromString(document.getString("uuid")), document.getString("name"));
        playerData.load(document);

        return playerData;
    }

    public String getAddress(UUID uuid) {
        Document document = plugin.getMongoManager().getProfilesCollection().find(Filters.eq("name", uuid.toString())).first();

        if (document == null) {
            return "";
        }

        return document.getString("address");
    }

    public Document getDocument(String name) {
        return plugin.getMongoManager().getProfilesCollection().find(Filters.eq("name", name.toLowerCase())).first();
    }

    public void setPermission(Object sender, Profile targetData, String permission, boolean set, boolean saveAfter, String server, long expire) {
        if (set) {
            if (targetData.isPermissionSet(permission)) {
                if (sender instanceof GlobalPlayer) {
                    ((GlobalPlayer) sender).sendMessage("that player already have that permission retard bro");
                } else if (sender instanceof Player) {
                    ((Player) sender).sendMessage("that player already have that permission retard bro");
                } else {
                    Bukkit.getConsoleSender().sendMessage("that player already have that permission retard bro");
                }
                return;
            }
            if (server == null || server.equalsIgnoreCase("global")) {
                targetData.getPermissions().add(new Permission(permission, null, true, expire != -1 ? System.currentTimeMillis() + expire : -1));
            } else {
                targetData.getPermissions().add(new Permission(permission, server, false, expire != -1 ? System.currentTimeMillis() + expire : -1));
            }
            if (sender instanceof GlobalPlayer) {
                ((GlobalPlayer) sender).sendMessage("you have set the permission");
            } else if (sender instanceof Player) {
                ((Player) sender).sendMessage("you have set the permission");
            } else {
                Bukkit.getConsoleSender().sendMessage("you have set the permission");
            }
        } else {
            if (!targetData.isPermissionSet(permission)) {
                if (sender instanceof GlobalPlayer) {
                    ((GlobalPlayer) sender).sendMessage("they dont have that permission idiota");
                } else if (sender instanceof Player) {
                    ((Player) sender).sendMessage("they dont have that permission idiota");
                } else {
                    Bukkit.getConsoleSender().sendMessage("they dont have that permission idiota");
                }
                return;
            }
            targetData.getPermissions().removeIf(perm -> perm.getPermission().equalsIgnoreCase(permission));
            if (sender instanceof GlobalPlayer) {
                ((GlobalPlayer) sender).sendMessage("one of your permissions have been removed");
            } else if (sender instanceof Player) {
                ((Player) sender).sendMessage("permsision removed");
            } else {
                Bukkit.getConsoleSender().sendMessage("permission removed");
            }
        }
        if (saveAfter) {
            targetData.save();
        }
    }
}
