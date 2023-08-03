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

public class RankPermissionsCommand extends SubCommand {
    public RankPermissionsCommand() {
        super("perms", Collections.singletonList("permissions"), "oxygen.rank.permissions");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.usage")));
            return;
        }

        if (args[1].equalsIgnoreCase("add")) {
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            for (String permissions : OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getActivePermissions()) {
                if (permissions.equalsIgnoreCase(args[3])) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.already-has")));
                    return;
                }
            }

            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.added").replace("%permission%", args[3])));
            Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[2]);
            rank.getActivePermissions().add(args[3]);
            rank.save();

            OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[2]));
        } else if (args[1].equalsIgnoreCase("list")) {
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getActivePermissions().size() == 0) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.no-permissions")));
                return;
            }

            for (String permissions : OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getActivePermissions()) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.list-format").replace("%permission%", permissions)));
            }
        } else if (args[1].equalsIgnoreCase("remove")) {
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            if (OxygenBukkit.getInstance().getRankManager().getRank(args[2]).getActivePermissions().size() == 0) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.no-permissions")));
                return;
            }
            Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[2]);
            if (!rank.getActivePermissions().contains(args[3])) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.not-have")));
                return;
            }

            rank.getActivePermissions().remove(args[3]);
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[2]));
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.removed")));
        } else {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
        }
    }
}

