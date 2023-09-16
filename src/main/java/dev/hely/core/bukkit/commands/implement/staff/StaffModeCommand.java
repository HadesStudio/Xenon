package dev.hely.core.bukkit.commands.implement.staff;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.staff
//   Date: Saturday, August 26, 2023 - 4:26 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffModeCommand extends BaseCommand {
    public StaffModeCommand() {
        super("staffmode", Arrays.asList("h", "modmode", "mod", "staff"), "oxygen.command.staffmode");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            if (OxygenBukkit.getInstance().getStaffManager().isStaffModeActive(player)) {
                OxygenBukkit.getInstance().getStaffManager().disableStaffMode(player);
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-mode.disabled")));
            } else {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-mode.enabled")));
                OxygenBukkit.getInstance().getStaffManager().enableStaffMode(player);
            }
        } else if (sender.hasPermission("oxygen.command.staffmode.others")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (!this.checkPlayer(sender, target, args[0])) return;

            if (OxygenBukkit.getInstance().getStaffManager().isStaffModeActive(target.getPlayer())) {
                OxygenBukkit.getInstance().getStaffManager().disableStaffMode(target.getPlayer());
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-mode.others.disabled").replace("%player%", sender.getName())));
            } else {
                OxygenBukkit.getInstance().getStaffManager().enableStaffMode(target.getPlayer());
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-mode.others.enabled").replace("%player%", sender.getName())));
            }
        }
    }
}
