package dev.hely.core.punishment;

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import dev.hely.lib.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
public class PunishmentListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            Punishment punishment = profile.getActiveMute();
            if (punishment != null) {
                event.setCancelled(true);

                if (punishment.isPermanent()) {
                    player.sendMessage(CC.translate("&cYou are permanently muted."));
                } else {
                    player.sendMessage(CC.translate("&cYou are currently muted for another &l" + punishment.getRemaining() + "&c."));
                }
            }
        }
    }
}
