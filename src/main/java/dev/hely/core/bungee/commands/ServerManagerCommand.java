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
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.utils.BungeeCC;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ServerManagerCommand extends Command {
    public ServerManagerCommand() {
        super("servermanager", "oxygen.bungee.servermanager", "sd", "deploy", "manager");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        for (Proxy proxy : OxygenBungee.getInstance().getServerManager().getProxies()) {
            commandSender.sendMessage(BungeeCC.translate("&cProxy Server: &6" + proxy.getName()));
        }
        for (Server server : OxygenBungee.getInstance().getServerManager().getServers()) {
            commandSender.sendMessage(BungeeCC.translate("&cBukkit Servers: &6" + server.getName()));
        }
    }
}
