package dev.hely.core.global.profile.note;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.global.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name OxygenGlobal
 */
public class NoteListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (profile != null) {
            if (profile.getNotes().size() > 0) {

            }
        }
    }
}
