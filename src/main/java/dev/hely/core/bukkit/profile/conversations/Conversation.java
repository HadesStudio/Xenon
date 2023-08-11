package dev.hely.core.bukkit.profile.conversations;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.staff
//   Date: Wednesday, August 09, 2023 - 2:41 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Conversation {
    private final UUID initiatedBy;
    private final UUID target;
    private long lastMessageSentAt;
    private UUID lastMessageSentBy;

    public Conversation(final UUID initiatedBy, final UUID target) {
        this.initiatedBy = initiatedBy;
        this.target = target;
        this.lastMessageSentAt = System.currentTimeMillis();
        final Profile initiatorProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(initiatedBy);
        initiatorProfile.getConversations().getConversations().put(target, this);
        final Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(target);
        targetProfile.getConversations().getConversations().put(initiatedBy, this);
    }

    public void sendMessage(final Player sender, final Player target, final String message) {
        final Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(sender.getUniqueId());
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.to-format").replace("%target%", target.getDisplayName()).replace("%message%", message)));
        final Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(target.getUniqueId());
        if (targetProfile.getSettings().isPlayingMessageSounds()) {
            target.playSound(target.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 1.0f);
        }
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.from-format").replace("%target%", sender.getDisplayName()).replace("%message%", message)));
        final long millis = System.currentTimeMillis();
        targetProfile.setLastReceivedMessageMillis(millis);
        Bukkit.getOnlinePlayers().forEach(staff -> {
            Profile profileStaff = OxygenBukkit.getInstance().getProfileManager().getProfile(staff.getUniqueId());
            if (profileStaff.getStaffProfile().isSocialSpy()) {
                staff.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-spy.message").replace("%sender%", sender.getDisplayName()).replace("%target%", target.getDisplayName()).replace("%message%", message)));
            }
        });
        profile.setLastSentMessageMillis(System.currentTimeMillis());
        profile.setLastRepliedTo(targetProfile.getName());
        profile.save();
        targetProfile.setLastReceivedMessageMillis(System.currentTimeMillis());
        targetProfile.setLastRepliedTo(profile.getName());
        targetProfile.save();
        this.lastMessageSentAt = System.currentTimeMillis();
        this.lastMessageSentBy = sender.getUniqueId();
    }

    public boolean validate() {
        final Player initiator = Bukkit.getPlayer(this.initiatedBy);
        if (initiator == null || !initiator.isOnline()) {
            this.destroy();
            return false;
        }
        final Player target = Bukkit.getPlayer(this.target);
        if (target == null || !target.isOnline()) {
            this.destroy();
            return false;
        }
        return true;
    }

    public void destroy() {
        for (final Player player : new Player[] { Bukkit.getPlayer(this.initiatedBy), Bukkit.getPlayer(this.target) }) {
            if (player != null && player.isOnline()) {
                final Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
                profile.getConversations().getConversations().remove(this.getPartner(player.getUniqueId()));
            }
        }
    }

    public UUID getPartner(final UUID compareWith) {
        if (this.initiatedBy.equals(compareWith)) {
            return this.target;
        }
        if (this.target.equals(compareWith)) {
            return this.initiatedBy;
        }
        return null;
    }

    public UUID getInitiatedBy() {
        return this.initiatedBy;
    }

    public UUID getTarget() {
        return this.target;
    }

    public long getLastMessageSentAt() {
        return this.lastMessageSentAt;
    }

    public UUID getLastMessageSentBy() {
        return this.lastMessageSentBy;
    }
}
