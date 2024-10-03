package net.ascend.core.bukkit.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.server.packet.impl.ServerStateChangePacket;

@Getter
@Setter
@RequiredArgsConstructor
public class Server {

    private String name = "";
    private long lastHeartbeat = System.currentTimeMillis();
    private long startTime = 0;
    private ServerState state;
    private int onlinePlayers = 0;
    private int maxPlayers = 0;
    private double tps = 0D;
    private long usedMemory = 0L;
    private long allocatedMemory = 0L;

    public Server(String name, boolean init) {
        this.name = name;

        if (init) {
            this.state = ServerState.ONLINE;
            this.startTime = System.currentTimeMillis();

            XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerStateChangePacket(name, true));
        }
    }

    public boolean canJoin() {
        return this.maxPlayers > this.onlinePlayers;
    }

    public int getOnlinePlayers() {
        return isOnline() ? onlinePlayers : 0;
    }

    public boolean isOnline() {
        switch (state) {
            case ONLINE:
            case WHITELISTED:
                return true;
            default:
                return false;
        }
    }
}
