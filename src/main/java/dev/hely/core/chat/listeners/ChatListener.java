package dev.hely.core.chat.listeners;

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

import dev.hely.core.Oxygen;
import dev.hely.core.chat.ChatModule;
import dev.hely.core.chat.impl.ChatMessage;
import dev.hely.core.profile.Profile;
import dev.hely.core.utils.TimeFormatUtils;
import dev.hely.core.utils.TimeUtil;
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

        if (Oxygen.getInstance().getChatManager().getMutedCache().containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        Oxygen.getInstance().getChatManager().getMessageCache().put(event.getPlayer().getUniqueId(), new ArrayList<>());
        Oxygen.getInstance().getChatManager().getWarningsCache().put(event.getPlayer().getUniqueId(), new AtomicInteger(ChatModule.MAX_WARNINGS));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onAsyncChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (player.hasPermission(Oxygen.getInstance().getConfiguration().getConfiguration().getString("chat-system.staff-bypass-permission"))) {
            return;
        }

        if (profile.getStaffProfile().isStaffChat()) {
            return;
        }

        if (Oxygen.getInstance().getChatManager().isMuted() && !Oxygen.getInstance().getChatManager().isAllow_high_rank_talk()) {
            player.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("chat.muted")));
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission(Oxygen.getInstance().getConfiguration().getConfiguration().getString("chat-system.mute-bypass-permission")) && Oxygen.getInstance().getChatManager().isAllow_high_rank_talk()) {
            player.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("chat.only-high-rank")));
            event.setCancelled(true);
            return;
        }

        if (Oxygen.getInstance().getChatManager().getSlowTime() > 0) {
            if (Oxygen.getInstance().getChatManager().getSlowChatRemaining(player.getUniqueId()) > 0) {
                for (String message : Oxygen.getInstance().getLanguageConfig().getConfiguration().getStringList("chat.slow-chat")) {
                    player.sendMessage(CC.translate(message).replace("%time%", String.valueOf(Oxygen.getInstance().getChatManager().getSlowTime())).replace("%wait_time%", TimeFormatUtils.formatTime((int) Oxygen.getInstance().getChatManager().getSlowChatRemaining(player.getUniqueId()) / 1000)));
                }
                return;
            }

            Oxygen.getInstance().getChatManager().getSlowCache().put(player.getUniqueId(), System.currentTimeMillis());
        }

        String msg = event.getMessage().toLowerCase();

        if (Oxygen.getInstance().getChatManager().isFiltered(msg)) {
            for (Player players : Oxygen.getInstance().getServer().getOnlinePlayers()) {
                if (players.hasPermission(Oxygen.getInstance().getConfiguration().getConfiguration().getString("chat-system.staff-bypass-permission"))) {
                    players.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("chat.filtered-message-alert").replace("%format%", Oxygen.getInstance().getChatManager().formatChatDisplay(player, msg))));
                }
            }

            player.sendMessage(Oxygen.getInstance().getChatManager().formatChatDisplay(player, msg));
            event.setCancelled(true);
            return;
        }

        for (Pattern pattern : Oxygen.getInstance().getChatManager().getInappropriate().keySet()) {

            if (!pattern.matcher(event.getMessage().toLowerCase()).find()) {
                continue;
            }

            final String value = Oxygen.getInstance().getChatManager().getInappropriate().get(pattern);

            if (value != null) {
            }

            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("chat.blocked")));
            break;
        }

        Oxygen.getInstance().getChatManager().getMessageCache().get(player.getUniqueId()).add(new ChatMessage(System.currentTimeMillis(), event.getMessage()));
    }
}
