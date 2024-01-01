package net.ascend.core.bukkit.rank.rank.commands.subcommands;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankInfoCommand extends SubCommand {

    public RankInfoCommand() {
        super("info", Collections.singletonList("information"), "xenon.rank.info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setsuffix.usage")));
            return;
        }

        Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
        if (rank == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        for (String string : XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getStringList("commands.info.message")) {
            sender.sendMessage(CC.translate(string)
                    .replace("%rank%", rank.getName())
                    .replace("%type%", rank.getType().name())
                    .replace("%prefix%", getRankPrefix(rank))
                    .replace("%suffix%", getRankSuffix(rank))
                    .replace("%weight%", String.valueOf(rank.getWeight()))
                    .replace("%bungee_perms%", String.valueOf(rank.getBungeePermissions().size()))
                    .replace("%server_perms%", String.valueOf(rank.getPermissionsData().size()))
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
