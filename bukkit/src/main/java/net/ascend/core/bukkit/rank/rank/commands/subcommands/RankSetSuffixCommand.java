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
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankSetSuffixCommand extends SubCommand {

    public RankSetSuffixCommand() {
        super("setsuffix", Collections.singletonList("suffix"), "xenon.rank.setsuffix");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setsuffix.usage")));
            return;
        }

        Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
        if (rank == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        String new_suffix = args[2].replace("_", " ");
        rank.setSuffix(new_suffix);
        rank.save();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setsuffix.set")
                .replace("%suffix%", new_suffix)
        ));

        XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
    }
}
