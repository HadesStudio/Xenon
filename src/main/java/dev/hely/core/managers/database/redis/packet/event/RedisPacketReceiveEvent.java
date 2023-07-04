package dev.hely.core.managers.database.redis.packet.event;

import dev.hely.core.managers.database.redis.packet.RedisPacket;
import lombok.*;
import org.bukkit.event.*;

@RequiredArgsConstructor
@Getter
public class RedisPacketReceiveEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private final RedisPacket packet;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
