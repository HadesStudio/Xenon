package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankInheritancesCommand extends SubCommand {

    public RankInheritancesCommand() {
        super("inherit", Collections.singletonList("inheritances"), "xenon.rank.inherit");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.usage")));
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            for (String inheritances : rank.getInheritances()) {
                if (inheritances.equalsIgnoreCase(args[2])) {
                    sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.already-inherit")));
                    return;
                }
            }

            Rank inheritRank = XenonBukkit.getInstance().getRankManager().getRank(args[2]);
            if (inheritRank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.added-inherit")
                    .replace("%rank_name%", args[1])
                    .replace("%inherit_rank_name%", args[2])));

            rank.getInheritances().add(inheritRank.getName());
            XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
        } else if (args[0].equalsIgnoreCase("list")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (rank.getInheritances().isEmpty()) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.no-inherits")));
                return;
            }

            for (String inheritances : rank.getInheritances()) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.list-format").replace("%rank%", inheritances)));
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            Rank inheritRank = XenonBukkit.getInstance().getRankManager().getRank(args[2]);
            if (inheritRank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (rank.getInheritances().isEmpty()) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (!rank.getInheritances().contains(args[2])) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.not-have")));
                return;
            }

            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.removed-inherit")));

            rank.getInheritances().remove(inheritRank.getName());
            XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
        } else {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
        }
    }
}

