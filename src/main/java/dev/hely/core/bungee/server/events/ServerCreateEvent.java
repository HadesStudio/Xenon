package dev.hely.core.bungee.server.events;

import dev.hely.core.bungee.server.data.ServerData;
import net.md_5.bungee.api.plugin.Event;

public class ServerCreateEvent extends Event {
    private final ServerData serverData;

    public ServerCreateEvent(ServerData serverData) {
        this.serverData = serverData;
    }

    public ServerData getServerData() {
        return serverData;
    }
}
