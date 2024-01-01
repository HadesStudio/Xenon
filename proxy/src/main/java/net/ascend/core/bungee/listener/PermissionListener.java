package net.ascend.core.bungee.listener;

import net.ascend.core.bungee.XenonBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 05/08/2023
 * Protect Name Oxygen
 */
public class PermissionListener implements Listener {

    @EventHandler
    public void onPermissionCheck(PermissionCheckEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        List<String> permissions = XenonBungee.getInstance().getPlayerPermissions().getOrDefault(player.getUniqueId(), null);

        if (permissions != null) {
            if (permissions.contains(event.getPermission())) {
                event.setHasPermission(true);
            }
        }
    }
}
