package dev.hely.core.bukkit.server.queue.commands;

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

import com.google.gson.JsonObject;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.ServerState;
import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.core.bukkit.server.queue.QueueRank;
import dev.hely.core.bukkit.server.queue.packets.implement.QueueAddPlayerPacket;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.core.bungee.utils.BungeeUtil;
import dev.hely.lib.CC;
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
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("join-queue-command.usage")));
            return;
        }
        Queue queue = Queue.getByUuid(player.getUniqueId());
        if(queue != null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.in-queue").replace("%server%", args[0])));
            return;
        }
        queue = Queue.getByName(args[0]);
        if(queue == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.not-exist").replace("%server%", args[0])));
            return;
        }
        if(queue.getServerData() == null || !(queue.getServerData().getState() == ServerState.ONLINE)) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.offline").replace("%server%", args[0])));
            return;
        }
        if (queue.containsPlayer(player.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.already-queue")));
        }
        if (player.hasPermission("oxygen.queue.bypass")) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.bypassed-queue").replace("%server%", args[0])));
            BungeeUtil.sendToServer(player, args[0]);
            return;
        }
        QueueRank queueRank = OxygenBukkit.getInstance().getQueueManager().getPriority(player);

        JsonObject rankObject = new JsonObject();
        rankObject.addProperty("rankName", queueRank.getName());
        rankObject.addProperty("priority", queueRank.getPriority());

        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("uuid", player.getUniqueId().toString());
        playerObject.add("rank", rankObject);

        JsonObject queueObject = new JsonObject();
        queueObject.addProperty("name", queue.getName());
        queueObject.add("player", playerObject);

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new QueueAddPlayerPacket(queueObject));
    }
}
