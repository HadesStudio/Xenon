package net.ascend.core.bukkit.server.queue.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.queue
//   Date: Tuesday, August 15, 2023 - 7:28 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.ServerState;
import net.ascend.core.bukkit.server.queue.packet.implement.QueueJoinPacket;
import net.ascend.core.bukkit.server.queue.packet.implement.QueueSendPlayerPacket;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class JoinQueueCommand extends BaseCommand {
    public JoinQueueCommand(){
        super("joinqueue", Arrays.asList("jq", "play", "playserver", "joinserver", "serverjoin"), true);
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
        if (player.hasPermission("xenon.queue.bypass")) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.bypassed-queue").replace("%server%", args[0])));
            XenonBukkit.getInstance().getRedisModule().sendPacket(new QueueSendPlayerPacket(server.getName(), player.getUniqueId()));
            return;
        }
        if (XenonBukkit.getInstance().getQueueManager().isQueueingFor(player.getUniqueId(), server.getName())) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.already-queue")));
            return;
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new QueueJoinPacket(server.getName(), player.getUniqueId()));
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.joined").replace("%server_name%", server.getName())));
    }
}
