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
import dev.hely.core.bungee.utils.BungeeCC;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ServerManagerCommand extends Command {
    public ServerManagerCommand() {
        super("servermanager", "oxygen.bungee.servermanager", "sd", "deploy", "manager");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length < 1) {
            for (String message : OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getStringList("server-manager-command.usage")) {
                commandSender.sendMessage(BungeeCC.translate(message));
            }
            return;
        }
        if (strings[0].equalsIgnoreCase("list")) {
            OxygenBungee.getInstance().getServerManager().listGameInstances(commandSender);
        } else if (strings[0].equalsIgnoreCase("delete") && strings.length == 2) {
            OxygenBungee.getInstance().getServerManager().deleteServer(commandSender, strings[1].toLowerCase());
        } else if (strings[0].equalsIgnoreCase("create") && strings.length == 4) {
            OxygenBungee.getInstance().getServerManager().createServer(commandSender, strings[1].toLowerCase(),strings[2], Integer.parseInt(strings[3]));
        } else {
            for (String message : OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getStringList("server-manager-command.usage")) {
                commandSender.sendMessage(BungeeCC.translate(message));
            }
        }
    }
}
