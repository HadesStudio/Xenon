package net.ascend.core.bukkit.server.queue.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue.commands
//   Date: Wednesday, August 16, 2023 - 10:20 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.ServerState;
import net.ascend.core.bukkit.server.queue.packet.implement.QueueLeavePacket;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class LeaveQueueCommand extends BaseCommand {
    public LeaveQueueCommand(){
        super("leavequeue", Collections.singletonList("queueleave"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("join-queue-command.usage")));
            return;
        }
        Server server = XenonBukkit.getInstance().getServerManager().getServer(args[0]);
        if (server == null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.not-exist").replace("%server%", args[0])));
            return;
        }
        if (server.getState() == ServerState.OFFLINE) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.offline").replace("%server%", args[0])));
            return;
        }
        if (!server.isQueueEnabled()) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.cannot-queue").replace("%server%", args[0])));
            return;
        }
        if (!XenonBukkit.getInstance().getQueueManager().isQueueingFor(player.getUniqueId(), server.getName())) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.not-in").replace("%server_name%", args[0])));
            return;
        }
        XenonBukkit.getInstance().getRedisModule().sendPacket(new QueueLeavePacket(server.getName(), player.getUniqueId()));
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.removed").replace("%server%", args[0])));
    }
}
