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

public class RankBungeePermissionsCommand extends SubCommand {

    public RankBungeePermissionsCommand() {
        super("bungeeperms", Collections.singletonList("bungeepermissions"), "xenon.rank.bungeepermissions");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
            if (rank== null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            String permission = args[2];
            if (rank.hasPermission(permission)) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.already-has")));
                return;
            }

            rank.getPermissionsData().add(new PermissionData(permission, true, new ArrayList<>()));
            rank.save();

            XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));


            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.added").replace("%permission%", args[3])));

        } else if (args[0].equalsIgnoreCase("list")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (rank.getPermissionsData().isEmpty()) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.no-permissions")));
                return;
            }

            if (sender instanceof Player) {
                new RankPermissionsMenu(rank).openMenu((Player) sender);
            }else {
                for (PermissionData permissions : rank.getPermissionsData()) {
                    sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.list-format").replace("%permission%", permissions.getPermission())));
                }
            }

        } else if (args[0].equalsIgnoreCase("remove")) {
            Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                return;
            }

            if (rank.getPermissionsData().isEmpty()) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.no-permissions")));
                return;
            }

            if (!rank.hasPermission(args[2])) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.not-have")));
                return;
            }

            rank.getPermissionsData().removeIf(permissionData -> permissionData.getPermission().equalsIgnoreCase(args[3]));
            rank.save();

            XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));

            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.removed")));
        } else {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.bungee-permissions.usage")));
        }
    }
}

