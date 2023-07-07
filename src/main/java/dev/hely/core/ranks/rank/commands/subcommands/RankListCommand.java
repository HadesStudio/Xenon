package dev.hely.core.ranks.rank.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.rank.commands.subcommands
//   Date: Wednesday, July 05, 2023 - 3:01 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.ranks.rank.Rank;
import dev.hely.core.ranks.rank.menu.RankListMenu;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankListCommand extends SubCommand {
    public RankListCommand() {
        super("list", "oxygen.rank.list", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (Oxygen.getInstance().getRankManager().getRanks().size() == 0) {
            player.sendMessage(CC.translate("&cSomehow, their is no ranks created, please open a Hely Development discord ticket, as this should automatically create a default rank.."));
            return;
        }

        Oxygen.getInstance().debug_message("Attempting to open rank list menu for " + player.getName() + " containing " + Oxygen.getInstance().getRankManager().getRanks().size() + " ranks.", Oxygen.getInstance().getRankManager());

        for (Rank rank : Oxygen.getInstance().getRankManager().getRanks()) {
            Oxygen.getInstance().debug_message(" * " + rank.getName(), Oxygen.getInstance().getRankManager());
        }

        new RankListMenu().openMenu(player);
        Oxygen.getInstance().debug_message("Successfully opened rank list menu for " + player.getName() + " containing " + Oxygen.getInstance().getRankManager().getRanks().size() + " ranks.", Oxygen.getInstance().getRankManager());
    }
}
