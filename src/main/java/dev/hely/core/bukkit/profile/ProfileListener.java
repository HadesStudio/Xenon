package dev.hely.core.bukkit.profile;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.packet.implement.FriendSessionPacket;
import dev.hely.core.bukkit.profile.packet.implement.StaffJoinPacket;
import dev.hely.core.bukkit.profile.packet.implement.StaffLeavePacket;
import dev.hely.core.bukkit.profile.packet.implement.StaffSwitchPacket;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name OxygenGlobal
 */
@AllArgsConstructor
public class ProfileListener implements Listener {

    private OxygenBukkit plugin;

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();

        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.translate("&cYou already online, this is an error? contact with administrators."));
            return;
        }

        Profile profile = plugin.getProfileManager().getProfile(uuid, name);

        if (profile == null || !profile.isLoaded()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.translate("&cFailed to load your profile"));
            return;
        }

        if (onPunishmentKick(event, profile, uuid)) return;

        profile.setLastSeen(System.currentTimeMillis());

        if (!profile.getCurrentAddress().equalsIgnoreCase(event.getAddress().getHostAddress())) {
            profile.setCurrentAddress(event.getAddress().getHostAddress());
        }

        if (!profile.getAddresses().contains(event.getAddress().getHostAddress())) {
            profile.getAddresses().add(event.getAddress().getHostAddress());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            profile.setupPermissionAttachments(player);

            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }

            String serverName = OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name");
            if (player.hasPermission("oxygen.staff.alerts")) {
                if (profile.getCurrentServer().equalsIgnoreCase("Unknown")) {
                    plugin.getRedisModule().sendPacket(new StaffJoinPacket(player.getName(), serverName));
                } else {
                    plugin.getRedisModule().sendPacket(new StaffSwitchPacket(player.getName(), serverName, profile.getCurrentServer()));
                }
            }

            if (!profile.getNotifications().isEmpty()) {
                OxygenBukkit.getInstance().getServer().getScheduler().runTaskLater(OxygenBukkit.getInstance(), () -> {
                    if (!player.isOnline()) {
                        return;
                    }

                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-system.notifications.join")) {
                        player.sendMessage(CC.translate(message.replace("%notification_amount%", String.valueOf(profile.getNotifications().size()))));
                    }
                }, 5);
            }

            OxygenBukkit.getInstance().debug_message("---------------------------------");
            OxygenBukkit.getInstance().debug_message("Debug Messages:");
            if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveHighestRank() != null) {
                String activeHighestRank = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveHighestRank().getName();
                OxygenBukkit.getInstance().debug_message("Active Highest Rank: " + activeHighestRank);
                if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveHighestRank().getPrefix() != null) {
                    OxygenBukkit.getInstance().debug_message(" - Prefix: " + OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveHighestRank().getPrefix());
                } else {
                    OxygenBukkit.getInstance().debug_message(" - Prefix: Null (URGENT ERROR)");
                }
                if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveHighestRank().getSuffix() != null) {
                    OxygenBukkit.getInstance().debug_message(" - Suffix: " + OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveHighestRank().getSuffix());
                } else {
                    OxygenBukkit.getInstance().debug_message(" - Suffix: Null (URGENT ERROR)");
                }

            } else {
                OxygenBukkit.getInstance().debug_message("Active Highest Rank: Null (URGENT ERROR)");
            }
            if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrant() != null) {
                Grant activeHighestRank = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrant();
                OxygenBukkit.getInstance().debug_message("Active Highest Grant: " + activeHighestRank.getRank().getName());
                if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrant().getRank().getPrefix() != null) {
                    OxygenBukkit.getInstance().debug_message(" - Prefix: " + OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrant().getRank().getPrefix());
                } else {
                    OxygenBukkit.getInstance().debug_message(" - Prefix: Null (URGENT ERROR)");
                }
                if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrant().getRank().getSuffix() != null) {
                    OxygenBukkit.getInstance().debug_message(" - Suffix: " + OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrant().getRank().getSuffix());
                } else {
                    OxygenBukkit.getInstance().debug_message(" - Suffix: Null (URGENT ERROR)");
                }

            } else {
                OxygenBukkit.getInstance().debug_message("Active Highest Grant: Null (URGENT ERROR)");
            }
            OxygenBukkit.getInstance().debug_message("");
            OxygenBukkit.getInstance().debug_message("All Grants: ");
            if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getGrants() != null) {
                for (Grant rank : OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getGrants()) {
                    if (rank.getRank() != null) {
                        OxygenBukkit.getInstance().debug_message(" - " + rank.getRank().getName() + " Rank Grant");
                    } else {
                        OxygenBukkit.getInstance().debug_message(" - Null (URGENT ERROR)");
                        OxygenBukkit.getInstance().debug_message(" - This user has " + OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getGrants().size() + " grants.");
                    }
                }
            } else {
                OxygenBukkit.getInstance().debug_message(" - Null (URGENT ERROR)");
            }
            OxygenBukkit.getInstance().debug_message("");
            OxygenBukkit.getInstance().debug_message("What the fuck?");
            OxygenBukkit.getInstance().debug_message("Love DevDipin");
            OxygenBukkit.getInstance().debug_message("---------------------------------");

            plugin.getRedisModule().sendPacket(new FriendSessionPacket(player.getName(), serverName, false));
            profile.setCurrentServer(serverName);

            profile.save();
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile.isLoaded()) {
            profile.setLastSeen(System.currentTimeMillis());

            String serverName = OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name");
            if (player.hasPermission("oxygen.staff.alerts")) {
                plugin.getRedisModule().sendPacket(new StaffLeavePacket(player.getName(), serverName));
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    profile.save(true);
                }
            }.runTaskAsynchronously(plugin);
        }
    }


    private boolean onPunishmentKick(AsyncPlayerPreLoginEvent event, Profile profile, UUID uuid) {
        if (profile.getActiveBlacklist() != null) {
            if (Locale.PUNISHMENTS_JOIN_ALLOW_BLACKLISTED) return false;
            Punishment punishment = profile.getActiveBlacklist();

            String finalMessage = Locale.PUNISHMENT_KICK.get("blacklist");

            finalMessage = finalMessage.replace("%target%", profile.getName());
            finalMessage = finalMessage.replace("%sender%", punishment.getAddedBy());
            finalMessage = finalMessage.replace("%reason%", punishment.getAddedReason());
            finalMessage = finalMessage.replace("%duration%", punishment.getRemaining());
            finalMessage = finalMessage.replace("%expire%", punishment.getRemaining());

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate(finalMessage));

            plugin.getProfileManager().getProfileMap().remove(uuid);
            return true;
        }

        if (profile.getActiveBan() != null) {
            if (Locale.PUNISHMENTS_JOIN_ALLOW_BANNED) return false;
            Punishment punishment = profile.getActiveBan();

            String finalMessage = Locale.PUNISHMENT_KICK.get("ban");

            finalMessage = finalMessage.replace("%target%", profile.getName());
            finalMessage = finalMessage.replace("%sender%", punishment.getAddedBy());
            finalMessage = finalMessage.replace("%reason%", punishment.getAddedReason());
            finalMessage = finalMessage.replace("%duration%", punishment.getRemaining());
            finalMessage = finalMessage.replace("%expire%", punishment.getRemaining());

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate(finalMessage));

            plugin.getProfileManager().getProfileMap().remove(uuid);
            return true;
        }

        // DETECT ALTS BANNED

        return false;
    }

}
