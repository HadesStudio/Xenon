package net.ascend.core.bukkit.client.listeners;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.client.listeners
//   Date: Saturday, July 15, 2023 - 8:12 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.client.impl.Client;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

public class ClientListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRegisterChannel(PlayerRegisterChannelEvent event) {
        String channel = event.getChannel();
        for(Client client : Client.values()) {
            if(client.getBrand() != null && client.getChannel().equalsIgnoreCase(channel)) {
                XenonBukkit.getInstance().getClientManager().getClients().put(event.getPlayer().getUniqueId(), client);
                return;
            }
        }
        if(!XenonBukkit.getInstance().getClientManager().getClients().containsKey(event.getPlayer().getUniqueId())) {
            XenonBukkit.getInstance().getClientManager().getClients().put(event.getPlayer().getUniqueId(), Client.VANILLA);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerQuit(PlayerQuitEvent event) {
        XenonBukkit.getInstance().getClientManager().getClients().remove(event.getPlayer().getUniqueId());
    }
}
