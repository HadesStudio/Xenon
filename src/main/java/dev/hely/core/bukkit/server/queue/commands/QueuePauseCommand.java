package dev.hely.core.bukkit.server.queue.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue.commands
//   Date: Tuesday, September 12, 2023 - 8:32 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class QueuePauseCommand extends BaseCommand {
    public QueuePauseCommand(){
        super("queuepause", Collections.singletonList("pausequeue"), "oxygen.queue.admin", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("pause-queue-command.usage")));
            return;
        }
        Server server = OxygenBukkit.getInstance().getServerManager().getServer(args[0]);
        if (server == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.not-exist").replace("%server%", args[0])));
            return;
        }

        if (server.isQueuePaused()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.pause.disabled").replace("%server%", args[0])));
            server.setQueuePaused(false);
        } else {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.pause.enabled").replace("%server%", args[0])));
            server.setQueuePaused(true);
        }
    }
}
