package dev.hely.core.bukkit.utils.actionbar;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ActionBarMessageEvent extends Event {
    private boolean cancelled;
    private static final HandlerList handlers;
    private String message;
    private final Player player;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void setMessage(String string) {
        this.message = string;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }

    static {
        handlers = new HandlerList();
    }

    public ActionBarMessageEvent(Player player, String string) {
        this.cancelled = false;
        this.player = player;
        this.message = string;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}