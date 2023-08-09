package dev.hely.core.bukkit.server;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.server.packet.implement.ServerUpdatePacket;
import dev.hely.core.bukkit.server.tasks.ServerTimeoutTask;
import dev.hely.core.bukkit.staff.requests.Request;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
public class ServerManager implements ModuleLoader {

    private boolean enabled;

    private String serverName;
    private String serverVersion;

    private List<Server> servers = new ArrayList<>();

    private OxygenBukkit plugin;

    @Override
    public void enable() {
        this.plugin = OxygenBukkit.getInstance();

        this.serverName = plugin.getConfiguration().getConfiguration().getString("server.name");
        this.serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        //new ServerUpdateThread(plugin).start();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new ServerTimeoutTask(), 20L, 20L);
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
        return "Server";
    }

    public Server getOrCreateServer(String name) {
        Server server = this.getServer(name);

        if (server != null) {
            return server;
        }

        this.servers.add(server);
        return server;
    }

    public Server getServer(String name) {
        return this.servers.stream().filter(server -> server.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public int getGlobalMaxPlayers() {
        return this.servers.stream().mapToInt(Server::getMaxPlayers).sum();
    }

    public int getGlobalPlayers() {
        return this.servers.stream().mapToInt(Server::getOnlinePlayers).sum();
    }

    public boolean isServerOnline(String name) {
        Server server = this.getServer(name);

        return server != null;
    }

    public String getServerStatus(String name) {
        Server server = this.getServer(name);

        if (server == null) return "&cOffline";
        if (server.isWhitelisted()) return "&7Whitelisted";
        return "&aOnline";
    }
}
