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
import dev.hely.core.bukkit.chat.ChatModule;
import dev.hely.core.bukkit.chat.impl.ChatMessage;
import dev.hely.core.bukkit.utils.TimeFormatUtils;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.lib.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private ChatModule chatModule;
    
    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {

        if (chatModule.getMutedCache().containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        chatModule.getMessageCache().put(event.getPlayer().getUniqueId(), new ArrayList<>());
        chatModule.getWarningsCache().put(event.getPlayer().getUniqueId(), new AtomicInteger(ChatModule.MAX_WARNINGS));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (player.hasPermission(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.staff-bypass-permission"))) {
            return;
        }

        if (profile.getStaffProfile().isStaffChat()) {
            return;
        }

        if (chatModule.isMuted() && !chatModule.isAllow_high_rank_talk()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.muted")));
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.mute-bypass-permission")) && chatModule.isAllow_high_rank_talk()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.only-high-rank")));
            event.setCancelled(true);
            return;
        }

        if (chatModule.getSlowTime() > 0) {
            if (chatModule.getSlowChatRemaining(player.getUniqueId()) > 0) {
                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat.slow-chat")) {
                    player.sendMessage(CC.translate(message).replace("%time%", String.valueOf(chatModule.getSlowTime())).replace("%wait_time%", TimeFormatUtils.formatTime((int) chatModule.getSlowChatRemaining(player.getUniqueId()) / 1000)));
                }
                return;
            }

            chatModule.getSlowCache().put(player.getUniqueId(), System.currentTimeMillis());
        }

        String msg = event.getMessage().toLowerCase();

        if (chatModule.isFiltered(msg)) {
            for (Player players : OxygenBukkit.getInstance().getServer().getOnlinePlayers()) {
                if (players.hasPermission(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.staff-bypass-permission"))) {
                    players.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.filtered-message-alert").replace("%format%", chatModule.formatChatDisplay(player, msg))));
                }
            }

            player.sendMessage(chatModule.formatChatDisplay(player, msg));
            event.setCancelled(true);
            return;
        }

        for (Pattern pattern : chatModule.getInappropriate().keySet()) {
            if (!pattern.matcher(event.getMessage().toLowerCase()).find()) {
                continue;
            }

            String value = chatModule.getInappropriate().get(pattern);

            if (value != null) {
            }

            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.blocked")));
            break;
        }

        chatModule.getMessageCache().get(player.getUniqueId()).add(new ChatMessage(System.currentTimeMillis(), event.getMessage()));
    }
}
