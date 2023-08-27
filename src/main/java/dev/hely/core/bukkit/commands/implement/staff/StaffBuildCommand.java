package dev.hely.core.bukkit.commands.implement.staff;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.staff
//   Date: Saturday, August 26, 2023 - 4:28 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffBuildCommand extends BaseCommand {

    public StaffBuildCommand() {
        super("staffbuild", "oxygen.command.staffbuild", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!OxygenBukkit.getInstance().getStaffManager().isStaffBuild(player)) {
            OxygenBukkit.getInstance().getStaffManager().enableStaffBuild(player);
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-build.enabled")));
        } else {
            OxygenBukkit.getInstance().getStaffManager().disableStaffBuild(player);
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-build.disabled")));
        }
    }
}
