package dev.hely.core.ranks.rank.commands.subcommands;

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

import dev.hely.core.Oxygen;
import dev.hely.core.ranks.rank.Rank;
import dev.hely.core.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankSetPrefixCommand extends SubCommand {

    public RankSetPrefixCommand() {
        super("setprefix", Collections.singletonList("prefix"), "oxygen.rank.setprefix");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setprefix.usage")));
            return;
        }

        if (Oxygen.getInstance().getRankManager().getRank(args[1]) == null) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        String new_prefix = args[2].replace("_", " ");
        Rank rank = Oxygen.getInstance().getRankManager().getRank(args[1]);
        rank.setPrefix(new_prefix);
        rank.save();

        sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setprefix.set")
                .replace("%prefix%", new_prefix)
        ));

        Oxygen.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
    }
}
