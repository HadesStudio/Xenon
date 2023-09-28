package dev.hely.core.bukkit.chat.listeners;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.chat.listeners
//   Date: Sunday, July 16, 2023 - 2:18 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.filter.Filter;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.packet.implement.AdminChatPacket;
import dev.hely.core.bukkit.profile.packet.implement.StaffChatPacket;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getStaffProfile().isStaffChat() || profile.getStaffProfile().isAdminChat()) {
            event.setCancelled(true);
            return;
        }

        if (OxygenBukkit.getInstance().getChatManager().isMuted() && !OxygenBukkit.getInstance().getChatManager().isAllow_high_rank_talk()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.muted")));
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.mute-bypass-permission")) && OxygenBukkit.getInstance().getChatManager().isAllow_high_rank_talk()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.only-high-rank")));
            event.setCancelled(true);
            return;
        }

        if (OxygenBukkit.getInstance().getChatManager().getSlowTime() > 0) {
            if (OxygenBukkit.getInstance().getChatManager().getSlowChatRemaining(player.getUniqueId()) > 0 && (!player.hasPermission(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.mute-bypass-permission")))) {
                event.setCancelled(true);
                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat.slow-chat")) {
                    player.sendMessage(CC.translate(message).replace("%time%", String.valueOf(OxygenBukkit.getInstance().getChatManager().getSlowTime())).replace("%wait_time%", TimeUtil.formatTime((int) OxygenBukkit.getInstance().getChatManager().getSlowChatRemaining(player.getUniqueId()) / 1000)));
                }
                return;
            }

            OxygenBukkit.getInstance().getChatManager().getSlowCache().put(player.getUniqueId(), System.currentTimeMillis());
        }

        String msg = event.getMessage();

        Filter filter = OxygenBukkit.getInstance().getFilterManager().checkMessage(msg);
        if (filter != null && !player.hasPermission("oxygen.admin")) {
            filter.chat_punish(msg, profile);

            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        event.getRecipients().forEach(recipient -> {
            Profile recipientProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(recipient.getUniqueId());
            if ((player != recipient && !player.hasPermission("oxygen.staff")) || recipientProfile.getIgnoredList().contains(player.getName())) {
                return;
            }

            recipient.sendMessage(OxygenBukkit.getInstance().getChatManager().formatChatDisplay(player, msg));
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleStaffChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            if (profile.getStaffProfile().isStaffChat()) {
                String message = event.getMessage();

                OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffChatPacket(player.getName(), OxygenBukkit.getInstance().getServer().getServerName(), message));

                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleAdminChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            if (profile.getStaffProfile().isAdminChat()) {
                String message = event.getMessage();

                OxygenBukkit.getInstance().getRedisModule().sendPacket(new AdminChatPacket(player.getName(), OxygenBukkit.getInstance().getServer().getServerName(), message));

                event.setCancelled(true);
            }
        }
    }
}
