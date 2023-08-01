package dev.hely.core.ranks.rank.commands.subcommands;

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

import dev.hely.core.Oxygen;
import dev.hely.core.ranks.rank.Rank;
import dev.hely.core.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class RankInheritancesCommand extends SubCommand {
    public RankInheritancesCommand() {
        super("inherit", Collections.singletonList("inheritances"), "oxygen.rank.inherit");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.usage")));
            return;
        }

        if (Oxygen.getInstance().getRankManager().getRank(args[1]) == null) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }
        for (String inheritances : Oxygen.getInstance().getRankManager().getRank(args[1]).getInheritances()) {
            if (inheritances.equalsIgnoreCase(args[2])) {
                sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.already-inherit")));

            }
        }
        if (Oxygen.getInstance().getRankManager().getRank(args[1]).getInheritances() == null) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        if (Oxygen.getInstance().getRankManager().getRank(args[2]) == null) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.created").replace("%rank_name%", args[1])));
        Rank rank = Oxygen.getInstance().getRankManager().getRank(args[1]);
        Rank inheritRank = Oxygen.getInstance().getRankManager().getRank(args[2]);

        rank.getInheritances().add(inheritRank.getName());
        Oxygen.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
    }
}

