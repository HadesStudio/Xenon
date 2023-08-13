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
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

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

        if (((ProxiedPlayer) commandSender).getServer().getInfo().getName().toLowerCase().contains(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("hub-name"))) {
            commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("hub-command.already-connected")));
            return;
        }

        ((ProxiedPlayer) commandSender).connect(OxygenBungee.getInstance().getServerManager().getRandomHub());
        commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("hub-command.connecting")));
    }
}
