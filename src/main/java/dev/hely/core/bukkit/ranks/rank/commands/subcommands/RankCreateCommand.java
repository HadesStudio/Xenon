package dev.hely.core.bukkit.ranks.rank.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.rank.commands.subcommands
//   Date: Wednesday, July 05, 2023 - 3:02 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankCreateCommand extends SubCommand {
    public RankCreateCommand() {
        super("create", Collections.singletonList("new"), "oxygen.rank.create");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.usage")));
            return;
        }

        if (OxygenBukkit.getInstance().getRankManager().getRank(args[1]) != null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.already-exist")));
            return;
        }

        OxygenBukkit.getInstance().getRankManager().createRank(args[1]);
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.created").replace("%rank_name%", args[1])));

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
    }
}

