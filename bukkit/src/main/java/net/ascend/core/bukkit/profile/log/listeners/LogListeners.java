package net.ascend.core.bukkit.profile.log.listeners;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.LogType;
import net.ascend.core.bukkit.rank.grant.event.GrantGivenEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public class LogListeners implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

            XenonBukkit.getInstance().getLogManager().logPlayerAction(
                    event.getPlayer().getUniqueId(),
                    event.getPlayer().getName(),
                    Arrays.asList(
                            "&6Executed By&7: &c" + event.getPlayer().getName(),
                            "&6Command&7: &c" + event.getMessage()
                    ),
                    LogType.EXECUTED_COMMAND, LogCategories.COMMAND_LOGS);

    }

    //---------------------------------------------------------//
    //                LEAVE AND JOIN LOGGING                   //
    //---------------------------------------------------------//
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(
                    event.getPlayer().getUniqueId(),
                    event.getPlayer().getName(),
                    Arrays.asList(
                            "&6Server&7: &c" + XenonBukkit.getInstance().getServerManager().getCurrentServerName()
                    ),
                    LogType.JOINED_SERVER, LogCategories.CONNECTION_LOGS);

    }

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(
                    event.getPlayer().getUniqueId(),
                    event.getPlayer().getName(),
                    Arrays.asList(
                            "&6Server&7: &c" + XenonBukkit.getInstance().getServerManager().getCurrentServerName()
                    ),
                    LogType.DISCONNECTED_SERVER, LogCategories.CONNECTION_LOGS);

    }

    //---------------------------------------------------------//
    //                 GRANT & RANK LOGGING                    //
    //---------------------------------------------------------//
    @EventHandler
    public void onGrantGivenEvent(GrantGivenEvent event) {

    }
}
