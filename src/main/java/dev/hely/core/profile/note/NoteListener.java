package dev.hely.core.profile.note;

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name Oxygen
 */
public class NoteListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (profile != null) {
            if (profile.getNotes().size() > 0) {

            }
        }
    }
}
