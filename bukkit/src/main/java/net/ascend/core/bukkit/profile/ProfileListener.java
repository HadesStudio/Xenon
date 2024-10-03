package net.ascend.core.bukkit.profile;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.packet.implement.FriendSessionPacket;
import net.ascend.core.bukkit.profile.packet.implement.staff.StaffJoinPacket;
import net.ascend.core.bukkit.profile.packet.implement.staff.StaffLeavePacket;
import net.ascend.core.bukkit.profile.packet.implement.staff.StaffSwitchPacket;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.punishment.packet.implement.PunishmentAttemptedJoinPacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.lib.sound.SoundManager;
import net.ascend.core.bukkit.lib.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
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
 * Protect Name Xenon
 */
@AllArgsConstructor
public class ProfileListener implements Listener {

    private XenonBukkit plugin;

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();

        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            String finalMessage = ChatUtil.buildDescription(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile.already-online"));

            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.translate(finalMessage));
            return;
        }

        Profile profile = plugin.getProfileManager().getProfile(uuid, name);

        if (profile == null || !profile.isLoaded()) {
            String finalMessage = ChatUtil.buildDescription(plugin.getLanguageConfig().getConfiguration().getStringList("profile.failed-load"));

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
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
           profile.setupPermissionAttachments(player);

            if (profile.getFirstSeen().equals(0L)) {
                profile.setFirstSeen(System.currentTimeMillis());
            }

            String serverName = XenonBukkit.getInstance().getServerManager().getCurrentServerName();
            if (player.hasPermission("xenon.staff.alerts")) {
                if (profile.getCurrentServer().equalsIgnoreCase("None")) {
                    plugin.getRedisModule().sendPacket(new StaffJoinPacket(player.getName(), serverName));
                } else {
                    plugin.getRedisModule().sendPacket(new StaffSwitchPacket(player.getName(), serverName, profile.getCurrentServer()));
                }
            }

            profile.setCurrentServer(serverName);
            profile.getSession().startTimings();
            profile.save();

            plugin.getRedisModule().sendPacket(new FriendSessionPacket(player.getName(), serverName, false));

            if (!profile.getNotifications().isEmpty()) { // FINISHED PLEASE
                SoundManager.INSTANCE.playSound(player, "NOTE_PLING");

                for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("notification.join")) {
                    player.sendMessage(CC.translate(message.replace("%notification_amount%", String.valueOf(profile.getNotifications().size()))));
                }
            }
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile.isLoaded()) {
            profile.setLastSeen(System.currentTimeMillis());

            String serverName = XenonBukkit.getInstance().getServerManager().getCurrentServerName();
            if (player.hasPermission("xenon.staff.alerts")) {
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
            XenonBukkit.getInstance().getRedisModule().sendPacket(new PunishmentAttemptedJoinPacket(profile.getUniqueId(), PunishmentType.BLACKLIST));

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
            XenonBukkit.getInstance().getRedisModule().sendPacket(new PunishmentAttemptedJoinPacket(profile.getUniqueId(), PunishmentType.BAN));
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
