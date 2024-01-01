package net.ascend.core.bukkit.profile.log.types;

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

import net.ascend.core.bukkit.XenonBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LogActionTypes {
    EXECUTED_COMMAND(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.used-command")
    ),
    OP_PLAYER_COMMAND(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.op-player-command")
    ),

    PUNISHMENT_RECEIVED(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.punishment-given")
    ),
    PUNISHMENT_REMOVED(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.punishment-removed")
    ),
    BANNED_PLAYER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.banned-player")
    ),
    UNBANNED_PLAYER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unbanned-player")
    ),
    BLACKLISTED_PLAYER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.blacklisted-player")
    ),
    UNBLACKLISTED_PLAYER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unblacklisted-player")
    ),
    MUTED_PLAYER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.muted-player")
    ),
    UNMUTED_PLAYER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.unmuted-player")
    ),
    KICKED_PLAYER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.kicked-player")
    ),
    WARNED_PLAYER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.warned-player")
    ),
    JOINED_SERVER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.joined-server")
    ),
    DISCONNECTED_SERVER(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.leave-server")
    ),
    GRANT_GIVEN(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.grant-given")
    ),
    GRANT_REMOVED(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.grant-removed")
    ),
    GRANT_EXPIRED(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.grant-expired")
    ),
    GAVE_RANK(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.given-rank")
    ),
    RECEIVED_CURRENCY(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.received-currency")
    ),
    REMOVED_CURRENCY(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.currency-removed")
    ),
    STAFF_UPDATED_CURRENCY(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-types.staff-currency-update")
    );

    @Getter private final String display_name;
}
