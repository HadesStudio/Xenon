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
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankInfoCommand extends SubCommand {

    public RankInfoCommand() {
        super("info", Collections.singletonList("information"), "oxygen.rank.info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setsuffix.usage")));
            return;
        }

        if (OxygenBukkit.getInstance().getRankManager().getRank(args[1]) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }
        Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[1]);

        for (String string : OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getStringList("commands.info.message")) {
            sender.sendMessage(CC.translate(string)
                    .replace("%rank%", rank.getName())
                    .replace("%prefix%", getRankPrefix(rank))
                    .replace("%suffix%", getRankSuffix(rank))
                    .replace("%weight%", String.valueOf(rank.getWeight()))
                    .replace("%bungee_perms%", String.valueOf(rank.getBungeePermissions().size()))
                    .replace("%server_perms%", String.valueOf(rank.getActivePermissions().size()))
                    .replace("%inherits%", String.valueOf(rank.getInheritances().size()))

            );

        }
    }

    public String getRankPrefix(Rank rank) {
        if (rank.getPrefix() == null) {
            return "Not Setup";
        } else {
            return rank.getPrefix();
        }
    }

    public String getRankSuffix(Rank rank) {
        if (rank.getSuffix() == null) {
            return "Not Setup";
        } else {
            return rank.getSuffix();
        }
    }
}
