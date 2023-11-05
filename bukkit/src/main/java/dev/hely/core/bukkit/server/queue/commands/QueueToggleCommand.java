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
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class QueueToggleCommand extends BaseCommand {
    public QueueToggleCommand(){
        super("queuetoggle", Collections.singletonList("queuestatus"), "oxygen.queue.admin", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("join-queue-command.usage")));
            return;
        }
        Server server = OxygenBukkit.getInstance().getServerManager().getServer(args[0]);
        if (server == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.not-exist").replace("%server%", args[0])));
            return;
        }

        if (server.isQueueEnabled()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.toggled.disabled").replace("%server%", args[0])));
            server.setQueueEnabled(false);
            server.save();
        } else {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.toggled.enabled").replace("%server%", args[0])));
            server.setQueueEnabled(true);
            server.save();
        }
    }
}
