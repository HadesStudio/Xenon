package dev.hely.core.bukkit.events;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.events
//   Date: Wednesday, August 09, 2023 - 2:21 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.packet.implement.StaffChatSpyMessagePacket;
import dev.hely.core.bukkit.profile.packet.implement.StaffMessagePacket;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;
import java.util.UUID;

public class PlayerMessageEvent extends Event implements Cancellable {
    private static final HandlerList handlers;
    private final Player sender;
    private final Player recipient;
    private final String message;
    private final boolean isReply;
    private boolean cancelled;

    public PlayerMessageEvent(final Player sender, final Set<Player> recipients, final String message, final boolean isReply, final UUID uuid) {
        this.cancelled = false;
        this.sender = sender;
        this.recipient = (Player) Iterables.getFirst(recipients, (Object)null);
        this.message = message;
        this.isReply = isReply;
    }

    public static HandlerList getHandlerList() {
        return PlayerMessageEvent.handlers;
    }

    public Player getSender() {
        return this.sender;
    }

    public Player getRecipient() {
        return this.recipient;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isReply() {
        return this.isReply;
    }

    public void send() {
        Preconditions.checkNotNull((Object)this.sender, "The sender cannot be null");
        Preconditions.checkNotNull((Object)this.recipient, "The recipient cannot be null");
        final Profile sendingUser = OxygenBukkit.getInstance().getProfileManager().getProfile(this.sender.getUniqueId());
        final Profile recipientUser = OxygenBukkit.getInstance().getProfileManager().getProfile(this.recipient.getUniqueId());
        sendingUser.setLastRepliedTo(recipientUser.getUniqueId());
        recipientUser.setLastRepliedTo(sendingUser.getUniqueId());
        final long millis = System.currentTimeMillis();
        recipientUser.setLastReceivedMessageMillis(millis);

        final String rank = ChatColor.translateAlternateColorCodes('&', "&f" + sendingUser.getActiveHighestRank().getPrefix());
        final String displayName = rank + this.sender.getDisplayName();
        final String rank2 = ChatColor.translateAlternateColorCodes('&', "&f" + recipientUser.getActiveHighestRank().getPrefix());
        final String displayName2 = rank2 + this.recipient.getDisplayName();
        final String[] array = { "kys", "nigger", "kill", "cunt", "hack", "phase", "aura", "killaura", "forceop", "exploit", "theboys", "antikb", "grief", "worldedit" };
        String[] array2;
        for (int length = (array2 = array).length, i = 0; i < length; ++i) {
            final String strings = array2[i];
            if (this.message.contains(strings)) {
                OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffChatSpyMessagePacket(
                        sendingUser.getName(),
                        recipientUser.getName(),
                        OxygenBukkit.getInstance().getServerManager().getServerName(),
                        ChatUtil.joinArray(this.message.replace(strings.toString(), new StringBuilder().toString()), " ", 1)));
                StaffMessage.broadcastFilter((CommandSender)this.sender, ChatColor.translateAlternateColorCodes('&', "&7&o-> " + this.recipient.getName() + "&7&o: &7&o" + this.message.replace(strings, new StringBuilder().append(ChatColor.RED).append(ChatColor.ITALIC).append(strings).append(ChatColor.GRAY).append(ChatColor.ITALIC).toString())), false);
                break;
            }
        }
        this.sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GRAY + "To " + displayName2 + ChatColor.GRAY + ") " + ChatColor.GRAY + this.message);
        this.recipient.sendMessage(ChatColor.GRAY + "(" + ChatColor.GRAY + "From " + displayName + ChatColor.GRAY + ") " + ChatColor.GRAY + this.message);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return PlayerMessageEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }
}

