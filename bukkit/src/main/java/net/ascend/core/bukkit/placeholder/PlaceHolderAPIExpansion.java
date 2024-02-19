package net.ascend.core.bukkit.placeholder;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.queue.Queue;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;

public class PlaceHolderAPIExpansion extends PlaceholderExpansion {

    private final XenonBukkit plugin;

    public PlaceHolderAPIExpansion(XenonBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "xenon";
    }

    @Override
    public String getAuthor() {
        return "Ascend LLC";
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

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

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

        // Punishment Placeholders:
        if (identifier.equalsIgnoreCase("punishment_ban_status")) {
            return getBanStatus(profile);
        }

        if (identifier.equalsIgnoreCase("punishment_ban_duration")) {
            if (profile.getActiveBan() != null) {
                if (profile.getActiveBan().isPermanent()) {
                    return "Permanent";
                } else {
                    return TimeUtil.getMoreDetailedTime(profile.getActiveBan().getDuration());
                }
            }
            return "N/A";
        }

        if (identifier.equalsIgnoreCase("punishment_mute_status")) {
            return getMuteStatus(profile);
        }

        if (identifier.equalsIgnoreCase("punishment_mute_duration")) {
            if (profile.getActiveMute() != null) {
                if (profile.getActiveMute().isPermanent()) {
                    return "Permanent";
                } else {
                    return TimeUtil.getMoreDetailedTime(profile.getActiveMute().getDuration());
                }
            }

            return "N/A";
        }

        if (identifier.equalsIgnoreCase("punishment_warnings_amount")) {
            return profile == null ? 0 + "" : profile.getPunishmentCountByType(PunishmentType.MUTE) + "";
        }

        // Profile Placeholders:
        if (identifier.equalsIgnoreCase("profile_name")) {
            return profile == null ? "Unknown?" : profile.getName();
        }
        if (identifier.equalsIgnoreCase("profile_currency")) {
            return profile == null ? 0 + "" : profile.getCurrency() + "";
        }
        if (identifier.equalsIgnoreCase("profile_tag_formated")) {
            return profile == null ? "Unknown" : profile.getTagFormated();
        }
        if (identifier.equalsIgnoreCase("profile_tag_name")) {
            return profile == null ? "Unknown" : profile.getTag();
        }

        if (identifier.equalsIgnoreCase("profile_active_highest_grant_duration")) {
            return profile == null ? 0 + "" : TimeUtil.getMoreDetailedTime(profile.getActiveRankGrant().getDuration()) + "";
        }
        if (identifier.equalsIgnoreCase("profile_active_highest_grant_name")) {
            return profile == null ? "Unknown" : profile.getActiveRankGrant().getRankName();
        }
        if (identifier.equalsIgnoreCase("profile_active_highest_grant_prefix")) {
            return profile == null ? "Unknown" : profile.getActiveRank().getPrefix();
        }
        if (identifier.equalsIgnoreCase("profile_active_highest_grant_suffix")) {
            return profile == null ? "Unknown" : profile.getActiveRank().getSuffix();
        }

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

    public String getBanStatus(Profile profile) {
        Punishment punishment = profile.getActiveBan();

        if (punishment != null) {
            if (punishment.isActive()) {
                if (punishment.isPermanent()) {
                    return "Permanent Ban";
                } else {
                    return "Temporary Ban";
                }
            } else {
                return "Not Active";
            }
        } else {
            return "N/A";
        }
    }


    public String getMuteStatus(Profile profile) {
        Punishment punishment = profile.getActiveMute();

        if (punishment != null) {
            if (punishment.isActive()) {
                if (punishment.isPermanent()) {
                    return "Permanent Mute";
                } else {
                    return "Temporary Mute";
                }
            } else {
                return "Not Active";
            }
        } else {
            return "N/A";
        }
    }
}
