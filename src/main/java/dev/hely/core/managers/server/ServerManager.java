package dev.hely.core.managers.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.managers.server
//   Date: Tuesday, July 04, 2023 - 5:04 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.*;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.modules.ModuleLoader;
import dev.hely.core.managers.server.global_player.GlobalPlayer;
import dev.hely.core.profile.Profile;
import dev.hely.core.utils.PluginUtils;
import dev.hely.lib.util.PluginUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ServerManager implements ModuleLoader {
    public static final String MODULE_NAME = "Server Manager";
    private boolean enabled;

    private List<Server> serverData = new ArrayList<>();
    private List<GlobalPlayer> globalPlayers = new ArrayList<>();


    @Override
    public void enable() {
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
        return this.enabled;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    public Server getServerData(String name) {
        return this.getServerData().stream().filter(data -> data.getServer_name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void removeInActivePlayers() {
        this.globalPlayers.removeIf(next -> System.currentTimeMillis() - next.getLastActivity() >= 6000L);
    }

    public void removeInActiveServers() {
        this.serverData.removeIf(next -> System.currentTimeMillis() - next.getLastTick() >= 15000L);
    }

    public Server getOrCreateServerData(String name) {
        Server data = this.getServerData(name);

        if (data != null) {
            return data;
        }
        data = new Server(name);
        this.serverData.add(data);
        return data;
    }

    public int getGlobalMaxPlayers() {
        return this.getServerData().stream().mapToInt(Server::getMax_players).sum();
    }

    public List<GlobalPlayer> getGlobalPlayers() {
        return new ArrayList<>(this.globalPlayers);
    }

    public List<Server> getServerData() {
        return new ArrayList<>(this.serverData);
    }

    public GlobalPlayer getGlobalPlayer(String name) {
        return this.getGlobalPlayers().stream().filter(data -> data.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public GlobalPlayer getGlobalPlayerWithDisguisedOrRealName(String name) {
        return this.getGlobalPlayers().stream().filter(data -> data.getName().equalsIgnoreCase(name)
                || data.getRealName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public GlobalPlayer getGlobalPlayer(UUID uuid) {
        return this.getGlobalPlayers().stream().filter(data -> data.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    public GlobalPlayer getOrCreateGlobalPlayer(String name, UUID uuid) {
        GlobalPlayer player = this.getGlobalPlayer(name);

        if (player != null) {
            return player;
        }
        player = new GlobalPlayer(uuid, name);
        this.globalPlayers.add(player);
        return player;
    }

    public GlobalPlayer getRealGlobalPlayer(String name) {
        Iterator<GlobalPlayer> iterator = this.globalPlayers.iterator();

        while (iterator.hasNext()) {
            GlobalPlayer next = iterator.next();

            if (next.getName().equalsIgnoreCase(name)) {
                return next;
            }
        }
        return null;
    }

    public int getPlayerCountFor(Player player) {
        int i = 0;

        for (Player online : PluginUtils.getOnlinePlayers()) {
            i++;
        }

        return i;
    }
}
