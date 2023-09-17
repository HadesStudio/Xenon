package dev.hely.core.bukkit.chat;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.chat
//   Date: Sunday, July 16, 2023 - 2:13 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.chat.listeners.ChatListener;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.lib.util.CC;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@Getter @Setter
public class ChatModule implements ModuleLoader {

    private boolean enabled;
    private int slowTime = 10;
    private boolean muted = false;
    private boolean allow_high_rank_talk = false;

    private Map<UUID, Long> slowCache = new HashMap<>();

    @Override
    public void enable() {
        OxygenBukkit.getInstance().getServer().getPluginManager().registerEvents(new ChatListener(), OxygenBukkit.getInstance());

        this.enabled = true;
    }


    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void reload() {
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Chat Manager";
    }

    public long getSlowChatRemaining(UUID uuid) {
        if (!this.slowCache.containsKey(uuid)) {
            return 0L;
        }

        return (this.slowCache.get(uuid) + (this.slowTime * 1000L)) - System.currentTimeMillis();
    }

    public String getVipFormat(Profile profile) {
        if (profile.hasVIPStatus()) {
            return OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.vip-format").replace("%symbol%", ChatColor.valueOf(profile.getVipColor()) + OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.settings.vip-icon"));
        } else {
            return "";
        }
    }

    public String formatChatDisplay(Player player, String message) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        String chatFormat = CC.translate(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.chat-format")
                .replace("%tags%", profile.getTag())
                .replace("%rank_prefix%", profile.getActiveHighestRank().getPrefix())
                .replace("%rank_suffix%", profile.getActiveHighestRank().getSuffix())
                .replace("%player%", player.getName())
                .replace("%vip%", getVipFormat(profile))
        );

        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? PlaceholderAPI.setPlaceholders(player, chatFormat) + message : chatFormat + message;
    }
}
