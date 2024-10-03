package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.implement.RankPermissionsMenu;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.rank.rank.permission.PermissionData;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class RankPermissionsCommand extends SubCommand {
    public RankPermissionsCommand() {
        super("perms", Collections.singletonList("permissions"), "xenon.rank.permissions");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.usage")));
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            String permission = args[2];
            if (rank.hasPermission(permission)) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.already-has")));
                return;
            }

            rank.getPermissionsData().add(new PermissionData(permission, false, new ArrayList<>()));
            rank.save();

            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.added").replace("%permission%", args[3])));

            XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
        } else if (args[0].equalsIgnoreCase("list")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (rank.getPermissionsData().isEmpty()) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.no-permissions")));
                return;
            }

            if (sender instanceof Player) {
                new RankPermissionsMenu(rank).openMenu((Player) sender);
            }else {
                for (PermissionData permissions : rank.getPermissionsData()) {
                    sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.list-format")
                            .replace("%permission%", permissions.getPermission())));
                }
            }

        } else if (args[0].equalsIgnoreCase("remove")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);

            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (rank.getPermissionsData().isEmpty()) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.no-permissions")));
                return;
            }

            if (!rank.hasPermission(args[2])) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.not-have")));
                return;
            }

            rank.getPermissionsData().removeIf(permissionData -> permissionData.getPermission().equalsIgnoreCase(args[3]));
            rank.save();

            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.permissions.removed")));

            XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
        } else {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
        }
    }
}

