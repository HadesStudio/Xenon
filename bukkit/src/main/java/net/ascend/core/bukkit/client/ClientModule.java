package net.ascend.core.bukkit.client;

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
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.client.impl.Client;
import net.ascend.core.bukkit.client.impl.Version;
import net.ascend.core.bukkit.client.listeners.ClientListener;
import net.ascend.core.bukkit.client.listeners.VersionListener;
import net.ascend.core.bukkit.modules.ModuleLoader;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ClientModule implements ModuleLoader {

    @Getter private Map<UUID, Client> clients = Maps.newHashMap();
    @Getter private Map<UUID, Version> versions = Maps.newHashMap();

    private boolean enabled;

    @Override
    public void onEnable() {
        XenonBukkit.getInstance().getServer().getPluginManager().registerEvents(new ClientListener(), XenonBukkit.getInstance());
        XenonBukkit.getInstance().getServer().getPluginManager().registerEvents(new VersionListener(), XenonBukkit.getInstance());

        this.enabled = true;
    }

    @Override
    public void onDisable() {
        this.enabled = false;
    }

    @Override
    public void onReload() {
    }

    public Client getPlayerClient(Player player) {
        return clients.get(player.getUniqueId());
    }

    public Version getPlayerVerision(Player player) {
        return versions.get(player.getUniqueId());
    }
}
