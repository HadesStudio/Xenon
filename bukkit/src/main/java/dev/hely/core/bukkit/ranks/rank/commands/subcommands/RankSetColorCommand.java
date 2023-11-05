package dev.hely.core.bukkit.ranks.rank.commands.subcommands;

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
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankSetColorCommand extends SubCommand {
    public RankSetColorCommand() {
        super("setcolor", Collections.singletonList("color"), "oxygen.rank.color");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.color.usage")));
            return;
        }
        if (OxygenBukkit.getInstance().getRankManager().getRank(args[1]) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.color.not-exist")));
            return;
        }

        Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[1]);

        ChatColor color;
        try {
            color = ChatColor.valueOf(args[2]);
        } catch (Exception e) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.color.invalid-color")));
            return;
        }
        rank.setDisplay_color(color);
        rank.save();

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.color.color-changed").replace("%rank%", args[1]).replace("%color%", color.name())));
        OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
    }
}

