package net.ascend.core.bukkit.server;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import lombok.Getter;
import net.ascend.core.bukkit.server.packet.impl.ServerStateChangePacket;
import net.ascend.core.bukkit.server.packet.impl.ServerUpdatePacket;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Getter
public class ServerManager implements ModuleLoader {

    private Set<Server> servers;

    private String currentServerName;

    private BukkitTask serverHeartbeatTask;

    @Override
    public void onEnable() {
        this.servers = new HashSet<>();

        this.currentServerName = XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("server-name");

        this.servers.add(new Server(this.currentServerName, true));

        this.serverHeartbeatTask = new ServerHeartbeatTask(XenonBukkit.getInstance(), this).runTaskTimerAsynchronously(XenonBukkit.getInstance(), 0L, 20L * 3);
    }

    @Override
    public void onDisable() {
        this.serverHeartbeatTask.cancel();

        this.getCurrentServer().setState(ServerState.OFFLINE);
        XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerUpdatePacket(this.getCurrentServer()));

        XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerStateChangePacket(this.currentServerName, false));
    }

    @Override
    public void onReload() {

    }

    public Server getCurrentServer() {
        return this.servers.stream().filter(server -> server.getName().equalsIgnoreCase(this.currentServerName)).findFirst().orElse(null);
    }

    public Server getServer(String name) {
        return this.servers.stream().filter(server -> server.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public int getGlobalPlayerCount() {
        return this.servers.stream()
                .filter(server -> !server.isOnline())
                .mapToInt(Server::getOnlinePlayers)
                .sum();
    }

    public int getRealPlayerCount() {
        int i = 0;

        /*for (Player online : Bukkit.getOnlinePlayers()) {
            if (XenonBukkit.getInstance().getStaffManager().isVanished(online) || XenonBukkit.getInstance().getStaffManager().isStaffModeActive(online)) {
                continue;
            }
            i++;
        }*/ // FINISHED

        return i;
    }

    public void updateServer(Server receivedServer) {
        Server server = this.getServer(receivedServer.getName());
        if(server == null) {
            server = new Server(receivedServer.getName(), false);
            this.servers.add(server);
        }

        server.setStartTime(receivedServer.getStartTime());
        server.setState(receivedServer.getState());
        server.setMaxPlayers(receivedServer.getMaxPlayers());
        server.setOnlinePlayers(receivedServer.getOnlinePlayers());
        server.setLastHeartbeat(receivedServer.getLastHeartbeat());
        server.setTps(receivedServer.getTps());
        server.setUsedMemory(receivedServer.getUsedMemory());
        server.setAllocatedMemory(receivedServer.getAllocatedMemory());
    }
}