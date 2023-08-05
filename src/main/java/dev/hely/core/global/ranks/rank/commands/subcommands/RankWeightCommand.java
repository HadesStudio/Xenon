package dev.hely.core.global.ranks.rank.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.rank.commands.subcommands
//   Date: Wednesday, August 02, 2023 - 5:39 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.global.ranks.rank.Rank;
import dev.hely.core.global.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankWeightCommand extends SubCommand {

    public RankWeightCommand() {
        super("setweight", Collections.singletonList("weight"), "oxygen.rank.weight");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setweight.usage")));
            return;
        }

        if (OxygenBukkit.getInstance().getRankManager().getRank(args[1]) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }
        try {
            Integer.parseInt(args[2]);
        } catch (NumberFormatException nfe) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setweight.usage")));
            return;
        }

        String new_weight = args[2];
        Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[1]);
        rank.setWeight(Integer.parseInt(new_weight));
        rank.save();

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setweight.set")
                .replace("%weight%", new_weight)
        ));

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
    }
}
