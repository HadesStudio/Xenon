package dev.hely.core.bungee.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.commands
//   Date: Sunday, August 13, 2023 - 12:58 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.utils.BungeeCC;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class HubCommand extends Command {
    public HubCommand() {
        super("hub", "", "lobby");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("only-player")));
            return;
        }
        final ProxiedPlayer player = (ProxiedPlayer) commandSender;

        final List<Server> hubServers = OxygenBungee.getInstance().getServerManager().getAvailableServers(player.getUniqueId(), OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("lobby-group")).stream().filter(it -> !it.getName().equalsIgnoreCase(OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("server-name"))).collect(Collectors.toList());
        if (hubServers.isEmpty()) {
            commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("hub-command.no-servers")));
            return;
        }
        ServerInfo target = ProxyServer.getInstance().getServerInfo(hubServers.get(ThreadLocalRandom.current().nextInt(hubServers.size())).getName());

        player.connect(target);
        commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("hub-command.connecting")));
    }
}
