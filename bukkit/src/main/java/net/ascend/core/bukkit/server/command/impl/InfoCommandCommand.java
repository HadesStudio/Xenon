package net.ascend.core.bukkit.server.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.util.TPSUtil;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Date;

public class InfoCommandCommand extends SubCommand {

    public InfoCommandCommand(){
        super("info", Collections.singletonList("status"), "xenon.command.servermanager.argument.info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.SERVER_MANAGER_INFO_USAGE));
            return;
        }

        Server server = XenonBukkit.getInstance().getServerManager().getServer(args[0]);
        if (server == null) {
            sender.sendMessage(CC.translate(Locale.SERVER_MANAGER_DONT_EXISTS));
            return;
        }

        for (String string : Locale.SERVER_MANAGER_INFO_MESSAGE) {
            string = string.replace("%server%", server.getName())
                    .replace("%status%", server.getState().getDisplayName())
                    .replace("%online_players%", String.valueOf(server.getOnlinePlayers()))
                    .replace("%max_players%", Integer.toString(server.getMaxPlayers()))
                    .replace("%tps%", TPSUtil.getTPSStatus(server.getTps()))
                    .replace("%start_time%", TimeUtil.formatIntoCalendarString(new Date(server.getStartTime())))
                    .replace("%uptime%", TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getStartTime()))
                    .replace("%last_heartbeat%", TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getLastHeartbeat()));

            sender.sendMessage(CC.translate(string));
        }
    }
}
