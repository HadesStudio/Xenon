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
import dev.hely.core.bukkit.ranks.rank.menu.RankPermissionsMenu;
import dev.hely.core.bukkit.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.bukkit.ranks.rank.permission.PermissionData;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class RankPermissionsCommand extends SubCommand {
    public RankPermissionsCommand() {
        super("perms", Collections.singletonList("permissions"), "oxygen.rank.permissions");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.usage")));
            return;
        }

        if (args[1].equalsIgnoreCase("add")) {
            Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[2]);

            if (rank == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            String permission = args[3];
            if (rank.hasPermission(permission)) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.already-has")));
                return;
            }

            rank.getPermissionsData().add(new PermissionData(permission, true, new ArrayList<>()));
            rank.save();

            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.added").replace("%permission%", args[3])));

            OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[2]));
        } else if (args[1].equalsIgnoreCase("list")) {
            Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[2]);
            if (rank == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }
            if (rank.getPermissionsData().size() == 0) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.no-permissions")));
                return;
            }

            if (sender instanceof Player) {
                new RankPermissionsMenu(rank).openMenu((Player) sender);
            }else {
                for (PermissionData permissions : rank.getPermissionsData()) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.list-format").replace("%permission%", permissions.getPermission())));
                }
            }

        } else if (args[1].equalsIgnoreCase("remove")) {
            Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[2]);

            if (rank == null) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (rank.getPermissionsData().size() == 0) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.no-permissions")));
                return;
            }

            if (!rank.hasPermission(args[3])) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.not-have")));
                return;
            }

            rank.getPermissionsData().removeIf(permissionData -> permissionData.getPermission().equalsIgnoreCase(args[3]));
            rank.save();

            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.removed")));

            OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[2]));
        } else {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
        }
    }
}

