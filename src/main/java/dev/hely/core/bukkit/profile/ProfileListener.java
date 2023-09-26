package dev.hely.core.bukkit.profile;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.packet.implement.FriendSessionPacket;
import dev.hely.core.bukkit.profile.packet.implement.StaffJoinPacket;
import dev.hely.core.bukkit.profile.packet.implement.StaffLeavePacket;
import dev.hely.core.bukkit.profile.packet.implement.StaffSwitchPacket;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.punishment.PunishmentType;
import dev.hely.core.bukkit.punishment.packet.implement.PunishmentAttemptedJoinPacket;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.lib.sound.SoundManager;
import dev.hely.lib.util.CC;
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
            String finalMessage = ChatUtil.buildDescription(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile.already-online"));

            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.translate(finalMessage));
            return;
        }

        Profile profile = plugin.getProfileManager().getProfile(uuid, name);

        if (profile == null || !profile.isLoaded()) {
            String finalMessage = ChatUtil.buildDescription(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile.failed-load"));

            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.translate(finalMessage));
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

        profile.setupAlts();
        if (profile.getAlts().size() == 0 || profile.getAlts().isEmpty()) {
            profile.setupAlts();
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

            if (!OxygenBukkit.getInstance().isLatestVersion()) {
                if (event.getPlayer().isOp()) {
                    event.getPlayer().sendMessage(CC.translate("&6[Hely Development] &7Your plugin is outdated! &6Oxygen " + OxygenBukkit.getInstance().getLatestVersionText() + " &7has been released!"));
                    event.getPlayer().sendMessage(CC.translate("&6[Hely Development] &7Please download the latest version from our discord! &6&nhttps://discord.helydev.com"));
                    SoundManager.INSTANCE.playSound(event.getPlayer(), "ANVIL_LAND");
                }
            }

            String serverName = OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name");
            if (player.hasPermission("oxygen.staff.alerts")) {
                if (profile.getCurrentServer().equalsIgnoreCase("Unknown")) {
                    plugin.getRedisModule().sendPacket(new StaffJoinPacket(player.getName(), serverName));
                } else {
                    plugin.getRedisModule().sendPacket(new StaffSwitchPacket(player.getName(), serverName, profile.getCurrentServer()));
                }
            }
            profile.getSession().startTimings();

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

            profile.getSession().stopTimings();
            profile.setCurrentServer("None");


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
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new PunishmentAttemptedJoinPacket(profile.getUniqueId(), PunishmentType.BLACKLIST));

            Punishment punishment = profile.getActiveBlacklist();

            String finalMessage = Locale.PUNISHMENT_KICK.get("blacklist");

            finalMessage = finalMessage.replace("%target%", profile.getName());
            finalMessage = finalMessage.replace("%sender%", punishment.getAddedByString());
            finalMessage = finalMessage.replace("%reason%", punishment.getAddedReason());
            finalMessage = finalMessage.replace("%duration%", punishment.getRemaining());
            finalMessage = finalMessage.replace("%expire%", punishment.getRemaining());

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate(finalMessage));

            plugin.getProfileManager().getProfileMap().remove(uuid);
            return true;
        }

        if (profile.getActiveBan() != null) {
            if (Locale.PUNISHMENTS_JOIN_ALLOW_BANNED) return false;
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new PunishmentAttemptedJoinPacket(profile.getUniqueId(), PunishmentType.BAN));
            Punishment punishment = profile.getActiveBan();

            String finalMessage = Locale.PUNISHMENT_KICK.get("ban");

            finalMessage = finalMessage.replace("%target%", profile.getName());
            finalMessage = finalMessage.replace("%sender%", punishment.getAddedByString());
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
