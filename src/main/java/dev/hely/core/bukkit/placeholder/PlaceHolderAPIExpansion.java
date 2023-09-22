package dev.hely.core.bukkit.placeholder;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.util.CC;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;

public class PlaceHolderAPIExpansion extends PlaceholderExpansion {

    private final OxygenBukkit plugin;

    public PlaceHolderAPIExpansion(OxygenBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "oxygen";
    }

    @Override
    public String getAuthor() {
        return "Hely Development";
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null || !player.isOnline()) return null;

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        // Staff Placeholders:
        if (identifier.equalsIgnoreCase("staff_mode")) {
            return !profile.getStaffProfile().isStaffMode() ? CC.translate("&aTrue") : CC.translate("&cFalse");
        }
        if (identifier.equalsIgnoreCase("staff_vanish")) {
            return !profile.getStaffProfile().isVanish() ? CC.translate("&aTrue") : CC.translate("&cFalse");
        }
        if (identifier.equalsIgnoreCase("staff_next_auth_needed")) {
            return !profile.isNeedsVerify() ? CC.translate("&aNow") : DurationFormatUtils.formatDurationWords(profile.getNextAuthenticationNeeded() - System.currentTimeMillis(), true, true);
        }

        // VIP Placeholders:
        if (identifier.equalsIgnoreCase("vip_access")) {
            return !profile.hasVIPStatus() ? CC.translate("&aTrue") : CC.translate("&cFalse");
        }
        if (identifier.equalsIgnoreCase("vip_cooldown")) {
            return !profile.hasVIPStatus() ? CC.translate("&aAvailable") : DurationFormatUtils.formatDurationWords(profile.getVipCooldown() - System.currentTimeMillis(), true, true);
        }
        if (identifier.equalsIgnoreCase("vip_color")) {
            return profile == null ? "None" + "" : profile.getVipColor() + "";
        }

        // Profile Placeholders:
        if (identifier.equalsIgnoreCase("profile_notifications")) {
            return profile == null ? 0 + "" : profile.getNotifications().size() + "";
        }
        if (identifier.equalsIgnoreCase("profile_friends_size")) {
            return profile == null ? 0 + "" : profile.getFriends().size() + "";
        }
        if (identifier.equalsIgnoreCase("profile_friend_requests_size")) {
            return profile == null ? 0 + "" : profile.getFriendRequests().size() + "";
        }
        if (identifier.equalsIgnoreCase("profile_playtime")) {
            return profile == null ? 0 + "" : TimeUtil.getMoreDetailedTime(profile.getTotalPlayTime()) + "";
        }
        if (identifier.equalsIgnoreCase("profile_queue_priority")) {
            return profile == null ? 0 + "" : profile.getQueuePriority() + "";
        }
        if (identifier.equalsIgnoreCase("profile_discord_synced")) {
            return !profile.isDiscordSynced() ? CC.translate("&aTrue") : CC.translate("&cFalse");
        }
        if (identifier.equalsIgnoreCase("profile_frozen")) {
            return !profile.isFrozen() ? CC.translate("&aTrue") : CC.translate("&cFalse");
        }
        if (identifier.equalsIgnoreCase("profile_vpn_whitelist")) {
            return !profile.isVpnWhitelist() ? CC.translate("&aTrue") : CC.translate("&cFalse");
        }

        // Server Shit:
        for (Server server : this.plugin.getServerManager().getServers().values()) {
            if (identifier.equalsIgnoreCase("queue_" + server.getName() + "_players_in_queue")) {
                return String.valueOf(server.getPlayersInQueue());
            }
            if (identifier.equalsIgnoreCase("queue_" + server.getName() + "_join_rate")) {
                return String.valueOf(server.getQueueRate());
            }
            if (identifier.equalsIgnoreCase("queue_position")) {
                Queue queue = this.plugin.getQueue();
                return String.valueOf(queue.getJoinPosition(profile));
            }
            if (identifier.equalsIgnoreCase("server_" + server.getName() + "_max_players")) {
                return String.valueOf(server.getMaxPlayers());
            }
            if (identifier.equalsIgnoreCase("server_" + server.getName() + "_players")) {
                return String.valueOf(server.getOnlinePlayers());
            }
            if (identifier.equalsIgnoreCase("server_" + server.getName() + "_state")) {
                return String.valueOf(server.getState().getDisplayName());
            }
        }
        return null;
    }

}
