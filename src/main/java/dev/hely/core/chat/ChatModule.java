package dev.hely.core.chat;

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

import dev.hely.core.Oxygen;
import dev.hely.core.chat.impl.ChatMessage;
import dev.hely.core.modules.ModuleLoader;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.profilesystem.impl.Message;
import dev.hely.lib.CC;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ChatModule implements ModuleLoader {
    @Getter private Oxygen instance;
    private boolean enabled;

    public static final int MAX_WARNINGS = Oxygen.getInstance().getConfiguration().getConfiguration().getInt("chat-system.warning-amount");
    public static final long AUTO_MUTE_DURATION = 60 * 1000L;

    @Getter @Setter private int slowTime = 10;
    @Getter @Setter private boolean muted = false;
    @Getter @Setter private boolean allow_high_rank_talk = false;

    @Getter @Setter private Map<UUID, Long> slowCache = new HashMap<>();
    @Getter @Setter private Map<UUID, Long> mutedCache = new HashMap<>();
    @Getter @Setter private Map<UUID, AtomicInteger> warningsCache = new HashMap<>();
    @Getter @Setter private Map<UUID, List<ChatMessage>> messageCache = new HashMap<>();

    @Getter private List<String> lowFilter;
    @Getter private List<String> linkWhitelist;

    private Pattern linkFilter;
    private Pattern ipFilter;

    @Getter
    private Map<Pattern, String> inappropriate = new HashMap<>();

    @Override
    public void enable() {
        this.instance = instance;

        this.inappropriate.put(Pattern.compile("n+[i1l|]+gg+[e3]+r+", Pattern.CASE_INSENSITIVE), "7d");
        this.inappropriate.put(Pattern.compile("k+i+l+l+ *y*o*u+r+ *s+e+l+f+", Pattern.CASE_INSENSITIVE), "7d");
        this.inappropriate.put(Pattern.compile("f+a+g+[o0]+t+", Pattern.CASE_INSENSITIVE), "7d");
        this.inappropriate.put(Pattern.compile("\\bk+y+s+\\b", Pattern.CASE_INSENSITIVE), "7d");
        this.inappropriate.put(Pattern.compile("b+e+a+n+e+r+", Pattern.CASE_INSENSITIVE), "7d");

        this.linkFilter = Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
        this.ipFilter = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

        this.lowFilter = Oxygen.getInstance().getConfiguration().getConfiguration().getStringList("chat-system.word-filters");
        this.linkWhitelist = Oxygen.getInstance().getConfiguration().getConfiguration().getStringList("chat-system.link-whitelists");

        instance.getServer().getOnlinePlayers().forEach(loopPlayer -> {
            this.messageCache.put(loopPlayer.getUniqueId(), new ArrayList<>());
            this.warningsCache.put(loopPlayer.getUniqueId(), new AtomicInteger(MAX_WARNINGS));
        });

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

        return (this.slowCache.get(uuid) + (this.slowTime * 1000)) - System.currentTimeMillis();
    }

    public boolean isFiltered(String message) {
        String msg = message.toLowerCase()
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("@", "a")
                .replace("7", "t")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replaceAll("\\p{Punct}|\\d", "").trim();

        String[] words = msg.trim().split(" ");

        for (String word : words) {
            for (String filteredWord : lowFilter) {
                if (word.contains(filteredWord)) {
                    return true;
                }
            }
        }

        for (String word : message.replace("(dot)", ".").replace("[dot]", ".").trim().split(" ")) {
            boolean continueIt = false;

            for (String phrase : this.linkWhitelist) {
                if (word.toLowerCase().contains(phrase)) {
                    continueIt = true;
                    break;
                }
            }

            if (continueIt) {
                continue;
            }

            if (ipFilter.matcher(word).matches() || linkFilter.matcher(word).matches()) {
                return true;
            }
        }
        return false;
    }

    public String isFilteredFind(String message) {
        String msg = message.toLowerCase()
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("@", "a")
                .replace("7", "t")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replaceAll("\\p{Punct}|\\d", "").trim();

        String[] words = msg.trim().split(" ");

        for (String word : words) {
            for (String filteredWord : lowFilter) {
                if (word.contains(filteredWord)) {
                    return word;
                }
            }
        }

        for (String word : message.replace("(dot)", ".").replace("[dot]", ".").trim().split(" ")) {
            boolean continueIt = false;

            for (String phrase : this.linkWhitelist) {
                if (word.toLowerCase().contains(phrase)) {
                    continueIt = true;
                    break;
                }
            }

            if (continueIt) {
                continue;
            }

            if (ipFilter.matcher(word).matches() || linkFilter.matcher(word).matches()) {
                return word;
            }
        }
        return null;
    }

    public String formatChatDisplay(Player player,String message) {
        final Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId());

        String chatFormat = CC.translate(Oxygen.getInstance().getConfiguration().getConfiguration().getString("chat-system.chat-format")
                .replace("%tags%", profile.getTag())
                .replace("%rank_prefix%", profile.getActiveHighestRank().getPrefix())
                .replace("%rank_suffix%", profile.getActiveHighestRank().getSuffix())
                .replace("%player%", player.getName())
        );

        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? PlaceholderAPI.setPlaceholders(player, chatFormat) + message : chatFormat + message;
    }
}
