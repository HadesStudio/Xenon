package net.ascend.core.bukkit.profile.log;

import net.ascend.core.bukkit.XenonBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LogType {

    // NORMALS

    JOINED_SERVER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.joined-server")
    ),
    DISCONNECTED_SERVER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.leave-server")
    ),
    EXECUTED_COMMAND(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.used-command")
    ),

    // PUNISHMENTS

    PUNISHMENT_RECEIVED(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.punishment-given")
    ),
    PUNISHMENT_REMOVED(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.punishment-removed")
    ),
    BANNED_PLAYER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.banned-player")
    ),
    UNBANNED_PLAYER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.unbanned-player")
    ),
    BLACKLISTED_PLAYER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.blacklisted-player")
    ),
    UNBLACKLISTED_PLAYER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.unblacklisted-player")
    ),
    MUTED_PLAYER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.muted-player")
    ),
    UNMUTED_PLAYER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.unmuted-player")
    ),
    KICKED_PLAYER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.kicked-player")
    ),
    WARNED_PLAYER(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.warned-player")
    ),

    // GRANTS

    GRANT_GIVEN(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.grant-given")
    ),
    GRANT_REMOVED(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.grant-removed")
    ),
    GAVE_RANK(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.given-rank")
    ),

    // CURRENCY

    RECEIVED_CURRENCY(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.received-currency")
    ),
    REMOVED_CURRENCY(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.currency-removed")
    ),
    STAFF_UPDATED_CURRENCY(
            XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("log.staff-currency-update")
    );

    @Getter private final String display_name;
}
