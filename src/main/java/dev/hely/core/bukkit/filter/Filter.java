package dev.hely.core.bukkit.filter;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.filter
//   Date: Thursday, September 14, 2023 - 12:59 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.filter.types.Match;
import dev.hely.core.bukkit.filter.types.MuteType;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.CC;
import lombok.Data;
import lombok.NonNull;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Filter implements Comparable<Filter> {

    private final UUID id;
    private String name;

    private String phrase;
    private Match match = Match.CONTAINS;
    private MuteType muteType = MuteType.NONE;
    private Pattern pattern = null;
    private long duration = TimeUnit.MINUTES.toMillis(5);
    private boolean enabled = true;
    private boolean caseSensitive = false;

    public Filter(String name, String phrase) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.phrase = phrase;
    }

    public void save() {
        save(false);
    }

    public void save(boolean remove) {
        Document document = new Document();
        document.put("id", id.toString());
        document.put("name", name);
        document.put("phrase", phrase);
        document.put("match", Match.valueOf(match.toString()).toString());
        document.put("muteType", MuteType.valueOf(muteType.toString()).toString());
        document.put("duration", duration);
        document.put("enabled", enabled);
        document.put("caseSensitive", caseSensitive);

        OxygenBukkit.getInstance().getMongoModule().getFiltersCollection().replaceOne(Filters.eq("id", id.toString()), document, new ReplaceOptions().upsert(true));
        if (remove) {
            OxygenBukkit.getInstance().getFilterManager().getFilters().remove(id);
        }
    }

    public boolean matches(String message) {
        if (!caseSensitive)
            message = message.toLowerCase();

        switch (match) {
            case CONTAINS:
                return message.contains(phrase);

            case EXACT:
                return message.equals(phrase);

            case STARTS_WITH:
                return message.startsWith(phrase);

            case ENDS_WITH:
                return message.endsWith(phrase);

            case REGEX:
                return pattern.matcher(message).find();
        }

        return false;
    }

    public void setPhrase(String phrase) {
        if (!caseSensitive && match != Match.REGEX)
            this.phrase = phrase.toLowerCase();
        else this.phrase = phrase;
    }

    @Override
    public int compareTo(@NonNull Filter other) {
        if (duration == -1 && other.duration == -1)
            return 0;

        if (other.duration == -1)
            return -1;

        if (duration == -1)
            return 1;

        if (duration == other.getDuration())
            return 0;

        return duration > other.getDuration() ? 1 : -1;
    }

    public String colorize(String message) {
        Matcher matcher;
        if (pattern != null)
            matcher = pattern.matcher(message);
        else matcher = Pattern.compile((match == Match.STARTS_WITH ? "^" : "") + phrase
                        + (match == Match.ENDS_WITH ? "$" : ""),
                caseSensitive ? 0 : Pattern.CASE_INSENSITIVE).matcher(message);

        while (matcher.find()) {
            message = message.replaceFirst(matcher.group(), CC.translate("&6&n") + matcher.group() + CC.translate("&6"));
        }
        return CC.translate("&6" + message);
    }

    public void chat_punish(String message, Profile sender) {
        String filter_message = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("filters.filtered-chat-message").replace("%sender%", sender.getName()).replace("%filtered_text%", message);

        if (muteType == MuteType.MUTE) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute " + sender.getName() + " " + TimeUtil.getDetailedTime(duration) + " Chat Filter -s");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("oxygen.staff")) {
                player.sendMessage(CC.translate(filter_message));
            }
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate(filter_message));
    }

    public void punish(String message, Profile sender, Profile target) {
        String filter_message = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("filters.filtered-message").replace("%sender%", sender.getName()).replace("%target%", target.getName()).replace("%filtered_text%", message);

        if (muteType == MuteType.MUTE) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute " + sender.getName() + " " + TimeUtil.getDetailedTime(duration) + " Chat Filter -s");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("oxygen.staff")) {
                player.sendMessage(CC.translate(filter_message));
            }
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate(filter_message));
    }
}
