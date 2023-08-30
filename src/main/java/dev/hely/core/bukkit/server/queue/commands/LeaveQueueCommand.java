package dev.hely.core.bukkit.server.queue.commands;

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

import com.google.gson.JsonObject;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.core.bukkit.server.queue.packets.implement.QueueRemovePlayerPacket;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
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
        Queue queue = Queue.getByUuid(player.getUniqueId());

        if(queue == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.not-in")));
            return;
        }

        JsonObject queueObj = new JsonObject();
        queueObj.addProperty("type", "remove_player");
        queueObj.addProperty("uuid", player.getUniqueId().toString());
        queueObj.addProperty("msg", true);

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new QueueRemovePlayerPacket(queueObj));
    }
}
