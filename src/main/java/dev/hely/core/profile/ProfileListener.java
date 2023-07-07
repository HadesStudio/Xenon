package dev.hely.core.profile;

import dev.hely.core.Oxygen;
import dev.hely.core.profile.packet.implement.StaffJoinPacket;
import dev.hely.core.profile.packet.implement.StaffLeavePacket;
import dev.hely.core.profile.packet.implement.StaffSwitchPacket;
import dev.hely.lib.CC;
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
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class ProfileListener implements Listener {

    private Oxygen plugin;

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

            String serverName = plugin.getServerManager().getServerName();
            if (player.hasPermission("oxygen.staff.alerts")) {
                if (profile.getCurrentServer().equalsIgnoreCase("Unknown")) {
                    plugin.getRedisModule().sendPacket(new StaffJoinPacket(player.getName(), serverName));
                } else {
                    plugin.getRedisModule().sendPacket(new StaffSwitchPacket(player.getName(), serverName, profile.getCurrentServer()));
                }
            }

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

            String serverName = plugin.getServerManager().getServerName();
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
}
