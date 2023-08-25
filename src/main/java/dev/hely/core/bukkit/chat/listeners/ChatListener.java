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
import dev.hely.core.bukkit.utils.TimeUtil;
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
    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {

        if (OxygenBukkit.getInstance().getChatManager().getMutedCache().containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        OxygenBukkit.getInstance().getChatManager().getMessageCache().put(event.getPlayer().getUniqueId(), new ArrayList<>());
        OxygenBukkit.getInstance().getChatManager().getWarningsCache().put(event.getPlayer().getUniqueId(), new AtomicInteger(ChatModule.MAX_WARNINGS));
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
            if (OxygenBukkit.getInstance().getChatManager().getSlowChatRemaining(player.getUniqueId()) > 0) {
                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat.slow-chat")) {
                    player.sendMessage(CC.translate(message).replace("%time%", String.valueOf(OxygenBukkit.getInstance().getChatManager().getSlowTime())).replace("%wait_time%", TimeUtil.formatTime((int) OxygenBukkit.getInstance().getChatManager().getSlowChatRemaining(player.getUniqueId()) / 1000)));
                }
                return;
            }

            OxygenBukkit.getInstance().getChatManager().getSlowCache().put(player.getUniqueId(), System.currentTimeMillis());
        }

        String msg = event.getMessage().toLowerCase();

        if (OxygenBukkit.getInstance().getChatManager().isFiltered(msg)) {
            for (Player players : OxygenBukkit.getInstance().getServer().getOnlinePlayers()) {
                if (players.hasPermission(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.staff-bypass-permission"))) {
                    players.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.filtered-message-alert").replace("%format%", OxygenBukkit.getInstance().getChatManager().formatChatDisplay(player, msg))));
                }
            }

            player.sendMessage(OxygenBukkit.getInstance().getChatManager().formatChatDisplay(player, msg));
            event.setCancelled(true);
            return;
        }

        for (Pattern pattern : OxygenBukkit.getInstance().getChatManager().getInappropriate().keySet()) {
            if (!pattern.matcher(event.getMessage().toLowerCase()).find()) {
                continue;
            }

            String value = OxygenBukkit.getInstance().getChatManager().getInappropriate().get(pattern);

            if (value != null) {
            }

            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.blocked")));
            break;
        }

        OxygenBukkit.getInstance().getChatManager().getMessageCache().get(player.getUniqueId()).add(new ChatMessage(System.currentTimeMillis(), event.getMessage()));
    }
}
