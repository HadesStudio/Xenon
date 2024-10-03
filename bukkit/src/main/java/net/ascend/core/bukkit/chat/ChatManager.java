package net.ascend.core.bukkit.chat;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.chat.listener.ChatListener;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.lib.CC;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.ascend.core.bukkit.tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter @Setter
public class ChatManager implements ModuleLoader {

    private boolean enabled;
    private int slowTime;
    private boolean muted = false;

    private Map<UUID, Long> slowCache = new HashMap<>();

    @Override
    public void onEnable() {
        this.slowTime = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("chat-system.default_slow");

        XenonBukkit.getInstance().getServer().getPluginManager().registerEvents(new ChatListener(this), XenonBukkit.getInstance());

        this.enabled = true;
    }


    @Override
    public void onDisable() {
        this.enabled = false;
    }

    @Override
    public void onReload() {
    }

    public long getSlowChatRemaining(UUID uuid) {
        if (!this.slowCache.containsKey(uuid)) {
            return 0L;
        }

        return (this.slowCache.get(uuid) + (this.slowTime * 1000L)) - System.currentTimeMillis();
    }

    public String formatChatDisplay(Player player, String message) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Tag tag = profile.getActiveTag();

        String chatFormat = CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.chat-format")
                .replace("%tags%", tag != null ? tag.getPrefix() : "")
                .replace("%rank_prefix%", profile.getActiveRank().getPrefix())
                .replace("%rank_suffix%", profile.getActiveRank().getSuffix())
                .replace("%player%", player.getName())
        );

        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? PlaceholderAPI.setPlaceholders(player, chatFormat) + message : chatFormat + message;
    }
}
