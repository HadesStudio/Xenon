package dev.hely.core.global.profile.log.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.log.types
//   Date: Monday, July 31, 2023 - 4:59 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.global.profile.log.category.LogCategories;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LogActionTypes {
    EXECUTED_COMMAND(
            true,
            LogCategories.COMMAND_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.used-command")
    ),
    OP_PLAYER_COMMAND(
            true,
            LogCategories.COMMAND_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.op-player-command")
    ),

    PUNISHMENT_RECEIVED(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.punishment-given")
    ),
    PUNISHMENT_REMOVED(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.punishment-removed")
    ),
    BANNED_PLAYER(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.banned-player")
    ),
    UNBANNED_PLAYER(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unbanned-player")
    ),
    BLACKLISTED_PLAYER(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.blacklisted-player")
    ),
    UNBLACKLISTED_PLAYER(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unblacklisted-player")
    ),
    MUTED_PLAYER(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.muted-player")
    ),
    UNMUTED_PLAYER(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unmuted-player")
    ),
    KICKED_PLAYER(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.kicked-player")
    ),
    WARNED_PLAYER(
            true,
            LogCategories.PUNISHMENT_LOGS,
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.warned-player")
    );

    @Getter private boolean logging;
    @Getter private LogCategories category;
    @Getter private String display_name;
}
