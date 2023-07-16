package dev.hely.core.client;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.client
//   Date: Saturday, July 15, 2023 - 7:54 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.collect.Maps;
import dev.hely.core.Oxygen;
import dev.hely.core.client.impl.Client;
import dev.hely.core.client.impl.Version;
import dev.hely.core.client.listeners.ClientListener;
import dev.hely.core.client.listeners.VersionListener;
import dev.hely.core.modules.ModuleLoader;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ClientModule implements ModuleLoader {
    @Getter private Map<UUID, Client> clients = Maps.newHashMap();
    @Getter private Map<UUID, Version> versions = Maps.newHashMap();

    private boolean enabled;

    @Override
    public void enable() {
        Oxygen.getInstance().getServer().getPluginManager().registerEvents(new ClientListener(), Oxygen.getInstance());
        Oxygen.getInstance().getServer().getPluginManager().registerEvents(new VersionListener(), Oxygen.getInstance());

        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void reload() {
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Client Manager";
    }

    public Client getPlayerClient(Player player) {
        return clients.get(player.getUniqueId());
    }
    public Version getPlayerVerision(Player player) {
        return versions.get(player.getUniqueId());
    }
}
