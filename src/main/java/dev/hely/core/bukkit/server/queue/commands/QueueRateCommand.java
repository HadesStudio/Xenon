package dev.hely.core.bukkit.server.queue.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue.commands
//   Date: Tuesday, September 12, 2023 - 8:42 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class QueueRateCommand extends BaseCommand {
    public QueueRateCommand(){
        super("queuerate", Collections.singletonList("queuejoinrate"), "oxygen.queue.admin", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("rate-queue-command.usage")));
            return;
        }
        Server server = OxygenBukkit.getInstance().getServerManager().getServer(args[0]);
        if (server == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.not-exist").replace("%server%", args[0])));
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate(Locale.COMMANDS_INVALID_NUMBER));
            return;
        }

        server.setQueueRate(amount);
        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.queue-rate-updated").replace("%server%", args[0]).replace("%amount%", args[1])));
    }

}
