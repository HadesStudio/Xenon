package dev.hely.core.bukkit.profile.log.types;

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
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LogActionTypes {
    EXECUTED_COMMAND(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.used-command")
    ),
    OP_PLAYER_COMMAND(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.op-player-command")
    ),

    PUNISHMENT_RECEIVED(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.punishment-given")
    ),
    PUNISHMENT_REMOVED(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.punishment-removed")
    ),
    BANNED_PLAYER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.banned-player")
    ),
    UNBANNED_PLAYER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unbanned-player")
    ),
    BLACKLISTED_PLAYER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.blacklisted-player")
    ),
    UNBLACKLISTED_PLAYER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unblacklisted-player")
    ),
    MUTED_PLAYER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.muted-player")
    ),
    UNMUTED_PLAYER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unmuted-player")
    ),
    KICKED_PLAYER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.kicked-player")
    ),
    WARNED_PLAYER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.warned-player")
    ),
    JOINED_SERVER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.joined-server")
    ),
    DISCONNECTED_SERVER(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.leave-server")
    ),
    GRANT_GIVEN(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.grant-given")
    ),
    GRANT_REMOVED(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.grant-removed")
    ),
    GRANT_EXPIRED(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.grant-expired")
    ),
    GAVE_RANK(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.given-rank")
    ),
    RECEIVED_CURRENCY(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.received-currency")
    ),
    REMOVED_CURRENCY(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.currency-removed")
    ),
    STAFF_UPDATED_CURRENCY(
            OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.staff-currency-update")
    );

    @Getter private final String display_name;
}
