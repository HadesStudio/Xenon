package dev.hely.core.global.ranks.rank.commands.subcommands;

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
import dev.hely.core.global.ranks.rank.Rank;
import dev.hely.core.global.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankInheritancesCommand extends SubCommand {
    public RankInheritancesCommand() {
        super("inherit", Collections.singletonList("inheritances"), "oxygen.rank.inherit");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.usage")));
            return;
        }

        if (args[1].equalsIgnoreCase("add")) {
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            for (String inheritances : OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getInheritances()) {
                if (inheritances.equalsIgnoreCase(args[3])) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.already-inherit")));
                    return;
                }
            }

            if (OxygenBukkit.getInstance().getRankManager().getRank(args[3]) == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.added-inherit").replace("%rank_name%", args[2]).replace("%inherit_rank_name%", args[3])));
            Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[2]);
            Rank inheritRank = OxygenBukkit.getInstance().getRankManager().getRank(args[3]);

            rank.getInheritances().add(inheritRank.getName());
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[2]));
        } else if (args[1].equalsIgnoreCase("list")) {
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getInheritances().size() == 0) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.no-inherits")));
                return;
            }

            for (String inheritances : OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getInheritances()) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.list-format").replace("%rank%", inheritances)));
            }
        } else if (args[1].equalsIgnoreCase("remove")) {
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[3]) == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getInheritances().size() == 0) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (!OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getInheritances().contains(args[3])) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.not-have")));
                return;
            }

            Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[2]);
            Rank inheritRank = OxygenBukkit.getInstance().getRankManager().getRank(args[3]);

            rank.getInheritances().remove(inheritRank.getName());
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[2]));
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.removed-inherit")));
        } else {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
        }
    }
}

