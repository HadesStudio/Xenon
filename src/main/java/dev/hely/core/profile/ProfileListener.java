package dev.hely.core.profile;

import dev.hely.core.Oxygen;
import dev.hely.lib.CC;
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
public class ProfileListener implements Listener {

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

        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(uuid, name);
        profile.setLastSeen(System.currentTimeMillis());

        if (profile == null || !profile.isLoaded()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.translate("&cFailed to load your profile"));
            return;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }
            profile.setupPermissionAttachments(player);
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.isLoaded()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    profile.save(true);
                }
            }.runTaskAsynchronously(Oxygen.getInstance());
        }
    }
}
