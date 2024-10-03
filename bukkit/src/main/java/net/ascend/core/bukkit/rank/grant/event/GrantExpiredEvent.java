package net.ascend.core.bukkit.rank.grant.event;

import net.ascend.core.bukkit.rank.grant.Grant;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class GrantExpiredEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Grant grant;

    public GrantExpiredEvent(Player player, Grant grant) {
        this.player = player;
        this.grant = grant;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
