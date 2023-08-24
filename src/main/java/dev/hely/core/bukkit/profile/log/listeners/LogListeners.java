package dev.hely.core.bukkit.profile.log.listeners;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.log.listeners
//   Date: Wednesday, August 23, 2023 - 4:58 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.log.category.LogCategories;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import dev.hely.core.bukkit.ranks.events.GrantGivenEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.UUID;

public class LogListeners implements Listener {
    //---------------------------------------------------------//
    //                COMMAND EXECUTION LOGGING                //
    //---------------------------------------------------------//
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        if (OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getBoolean("settings.log-commands")) {
            OxygenBukkit.getInstance().getLogManager().logPlayerAction(
                    event.getPlayer().getUniqueId(),
                    event.getPlayer().getName(),
                    Arrays.asList(
                            "&6Executed By&7: &a" + event.getPlayer().getName(),
                            "&6Command&7: &a" + event.getMessage()
                    ),
                    LogActionTypes.EXECUTED_COMMAND, LogCategories.COMMAND_LOGS);
        }
    }

    //---------------------------------------------------------//
    //                LEAVE AND JOIN LOGGING                   //
    //---------------------------------------------------------//
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getBoolean("settings.log-joins")) {
            OxygenBukkit.getInstance().getLogManager().logPlayerAction(
                    event.getPlayer().getUniqueId(),
                    event.getPlayer().getName(),
                    Arrays.asList(
                            "&6Server&7: &a" + OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name")
                    ),
                    LogActionTypes.JOINED_SERVER, LogCategories.CONNECTION_LOGS);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        if (OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getBoolean("settings.log-joins")) {
            OxygenBukkit.getInstance().getLogManager().logPlayerAction(
                    event.getPlayer().getUniqueId(),
                    event.getPlayer().getName(),
                    Arrays.asList(
                            "&6Server&7: &a" + OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name")
                    ),
                    LogActionTypes.DISCONNECTED_SERVER, LogCategories.CONNECTION_LOGS);
        }
    }

    //---------------------------------------------------------//
    //                 GRANT & RANK LOGGING                    //
    //---------------------------------------------------------//
    @EventHandler
    public void onGrantGivenEvent(GrantGivenEvent event) {
        OxygenBukkit.getInstance().getLogManager().logPlayerAction(
                event.getProfile().getUniqueId(),
                event.getProfile().getName(),
                Arrays.asList(
                        "&6Rank Name&7: &a" + event.getGrant().getRankName(),
                        "&6Rank Duration&7: &a" + event.getGrant().getRemaining(),
                        "&6Rank Adder&7: &a" + event.getGrant().getAdder(),
                        "&6Rank Server&7: &a" + event.getGrant().getServer()
                ),
                LogActionTypes.GRANT_GIVEN, LogCategories.RANK_LOGS);

        OxygenBukkit.getInstance().getLogManager().logPlayerAction(
                event.getGiver().getUniqueId(),
                event.getGiver().getName(),
                Arrays.asList(
                        "&6Rank Given&7: &a" + event.getGrant().getRankName(),
                        "&6Reason&7: &a" + event.getGrant().getReason(),
                        "&6Rank Added Time&7: &a" + event.getGrant().getAddedTime()
                ),
                LogActionTypes.GRANT_GIVEN, LogCategories.RANK_LOGS);
    }
}
