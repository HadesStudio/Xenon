package net.ascend.core.bukkit.rank.rank.commands.subcommands;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankBungeePermissionsCommand extends SubCommand {
    public RankBungeePermissionsCommand() {
        super("bungeeperms", Collections.singletonList("bungeepermissions"), "xenon.rank.bungeepermissions");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
            return;
        }

        if (args[1].equalsIgnoreCase("add")) {
            if (XenonBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            for (String permissions : XenonBukkit.getInstance().getRankManager().getRank(args[2]).getBungeePermissions()) {
                if (permissions.equalsIgnoreCase(args[3])) {
                    sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.already-has")));
                    return;
                }
            }

            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.added").replace("%permission%", args[3])));
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[2]);
            rank.getBungeePermissions().add(args[3]);
            rank.save();

            XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[2]));
        } else if (args[1].equalsIgnoreCase("list")) {
            if (XenonBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            if (XenonBukkit.getInstance().getRankManager().getRank(args[2]).getBungeePermissions().size() == 0) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.no-permissions")));
                return;
            }

            for (String permissions : XenonBukkit.getInstance().getRankManager().getRank(args[2]).getBungeePermissions()) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.list-format").replace("%permission%", permissions)));
            }
        } else if (args[1].equalsIgnoreCase("remove")) {
            if (XenonBukkit.getInstance().getRankManager().getRank(args[2]) == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            if (XenonBukkit.getInstance().getRankManager().getRank(args[2]).getBungeePermissions().size() == 0) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.no-permissions")));
                return;
            }
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[2]);
            if (!rank.getBungeePermissions().contains(args[3])) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.not-have")));
                return;
            }

            rank.getBungeePermissions().remove(args[3]);
            XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[2]));
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.removed")));
        } else {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
        }
    }
}

