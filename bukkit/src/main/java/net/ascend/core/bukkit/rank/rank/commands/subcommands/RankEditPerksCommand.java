package net.ascend.core.bukkit.rank.rank.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
// Ascend LLC Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: net.ascend.core.bukkit.rank.rank.commands.subcommands
//   Date: Sunday, January 21, 2024 - 8:27 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//   Ascend LLC Discord: https://discord.gg/xM8bktgC6q
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.perks.RankPerkEditMenu;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankEditPerksCommand extends SubCommand {
    public RankEditPerksCommand() {
        super("editperks", "xenon.rank.editperks", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.edit-perks.usage")));
            return;
        }
        Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
        if (rank == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        new RankPerkEditMenu(rank).openMenu(player);
    }
}