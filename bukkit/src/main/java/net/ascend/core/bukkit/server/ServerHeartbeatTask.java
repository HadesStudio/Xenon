package net.ascend.core.bukkit.server;

import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.server.packet.impl.ServerUpdatePacket;
import net.ascend.core.bukkit.util.TPSUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class ServerHeartbeatTask extends BukkitRunnable {

    private XenonBukkit plugin;
    private ServerManager serverManager;

    @Override
    public void run() {
        this.serverManager.getServers().stream().filter(server -> server.getState() == ServerState.OFFLINE)
                .forEach(server -> this.serverManager.getServers().remove(server));

        this.serverManager.getCurrentServer().setState(Bukkit.hasWhitelist() ? ServerState.WHITELISTED : ServerState.ONLINE);
        this.serverManager.getCurrentServer().setMaxPlayers(Bukkit.getMaxPlayers());
        this.serverManager.getCurrentServer().setOnlinePlayers(Bukkit.getOnlinePlayers().size());
        this.serverManager.getCurrentServer().setTps(TPSUtil.getRecentTps()[0]);
        this.serverManager.getCurrentServer().setLastHeartbeat(System.currentTimeMillis());
        this.serverManager.getCurrentServer().setUsedMemory((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 2L / 1048576L);
        this.serverManager.getCurrentServer().setAllocatedMemory(Runtime.getRuntime().totalMemory() / 1048576L);

        this.plugin.getRedisModule().sendPacket(new ServerUpdatePacket(this.serverManager.getCurrentServer()));
    }
}