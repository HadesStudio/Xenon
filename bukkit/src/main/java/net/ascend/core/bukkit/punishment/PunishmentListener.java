package net.ascend.core.bukkit.punishment;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.lib.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Xenon
 */
public class PunishmentListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
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
