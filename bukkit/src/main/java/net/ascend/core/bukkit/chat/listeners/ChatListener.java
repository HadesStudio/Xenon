package net.ascend.core.bukkit.chat.listeners;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.filter.Filter;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.packet.implement.AdminChatPacket;
import net.ascend.core.bukkit.profile.packet.implement.StaffChatPacket;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getStaffProfile().isStaffChat() || profile.getStaffProfile().isAdminChat()) {
            event.setCancelled(true);
            return;
        }

        if (XenonBukkit.getInstance().getChatManager().isMuted() && !XenonBukkit.getInstance().getChatManager().isAllow_high_rank_talk()) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.muted")));
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.mute-bypass-permission")) && XenonBukkit.getInstance().getChatManager().isAllow_high_rank_talk()) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.only-high-rank")));
            event.setCancelled(true);
            return;
        }

        if (XenonBukkit.getInstance().getChatManager().getSlowTime() > 0) {
            if (XenonBukkit.getInstance().getChatManager().getSlowChatRemaining(player.getUniqueId()) > 0 && (!player.hasPermission(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.mute-bypass-permission")))) {
                event.setCancelled(true);
                for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat.slow-chat")) {
                    player.sendMessage(CC.translate(message).replace("%time%", String.valueOf(XenonBukkit.getInstance().getChatManager().getSlowTime())).replace("%wait_time%", TimeUtil.formatTime((int) XenonBukkit.getInstance().getChatManager().getSlowChatRemaining(player.getUniqueId()) / 1000)));
                }
                return;
            }

            XenonBukkit.getInstance().getChatManager().getSlowCache().put(player.getUniqueId(), System.currentTimeMillis());
        }

        String msg = event.getMessage();

        Filter filter = XenonBukkit.getInstance().getFilterManager().checkMessage(msg);
        if (filter != null && !player.hasPermission("xenon.admin")) {
            filter.chat_punish(msg, profile);

            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        event.getRecipients().forEach(recipient -> {
            Profile recipientProfile = XenonBukkit.getInstance().getProfileManager().getProfile(recipient.getUniqueId());
            if ((player != recipient && !player.hasPermission("xenon.staff")) || recipientProfile.getIgnoredList().contains(player.getName())) {
                return;
            }

            recipient.sendMessage(XenonBukkit.getInstance().getChatManager().formatChatDisplay(player, msg));
            XenonBukkit.getInstance().getServer().getConsoleSender().sendMessage(XenonBukkit.getInstance().getChatManager().formatChatDisplay(player, msg));
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleStaffChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            if (profile.getStaffProfile().isStaffChat()) {
                String message = event.getMessage();

                XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffChatPacket(player.getName(), profile.getCurrentServer(), message));

                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleAdminChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            if (profile.getStaffProfile().isAdminChat()) {
                String message = event.getMessage();

                XenonBukkit.getInstance().getRedisModule().sendPacket(new AdminChatPacket(player.getName(), profile.getCurrentServer(), message));

                event.setCancelled(true);
            }
        }
    }
}
