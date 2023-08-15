package dev.hely.core.bungee.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.commands
//   Date: Tuesday, August 15, 2023 - 1:54 PM
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

public class ServerCommand extends Command {
    public ServerCommand() {
        super("server", "oxygen.bungee.server", "joinserver");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("only-player")));
            return;
        }
        if (strings.length == 0) {
            commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-command.usage")));
            return;
        }
        Server server = OxygenBungee.getInstance().getServerManager().serverFromName(strings[0]);
        final ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if (server == null) {
            commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-command.invalid-server").replace("%server%", strings[0])));
            return;
        }
        ServerInfo target = ProxyServer.getInstance().getServerInfo(strings[0]);
        player.connect(target);
        commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-command.sent").replace("%server%", target.getName())));
    }
}

