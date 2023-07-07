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
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import dev.hely.lib.JavaUtil;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankCreateCommand extends SubCommand {
    public RankCreateCommand() {
        super("create", Collections.singletonList("new"), "oxygen.rank.list");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.usage")));
            return;
        }

        String rank_name = args[1];

        if (Oxygen.getInstance().getRankManager().getRank(rank_name) != null) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.already-exist")));
            return;
        }

        if (!JavaUtil.isAlphanumeric(rank_name)) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.invalid-string")));
            return;
        }

        Oxygen.getInstance().getRankManager().createRank(rank_name);
        sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.created").replace("%rank_name%", rank_name)));
    }
}

