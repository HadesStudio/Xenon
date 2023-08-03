package dev.hely.core.client.listeners;

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

import dev.hely.core.Oxygen;
import dev.hely.core.client.impl.Client;
import dev.hely.core.ranks.grants.Grant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

public class ClientListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRegisterChannel(PlayerRegisterChannelEvent event) {
        String channel = event.getChannel();
        for(Client client : Client.values()) {
            if(client.getBrand() != null && client.getChannel().equalsIgnoreCase(channel)) {
                Oxygen.getInstance().getClientManager().getClients().put(event.getPlayer().getUniqueId(), client);
                return;
            }
        }
        if(!Oxygen.getInstance().getClientManager().getClients().containsKey(event.getPlayer().getUniqueId())) {
            Oxygen.getInstance().getClientManager().getClients().put(event.getPlayer().getUniqueId(), Client.VANILLA);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerQuit(PlayerQuitEvent event) {
        Oxygen.getInstance().getClientManager().getClients().remove(event.getPlayer().getUniqueId());
    }
}
