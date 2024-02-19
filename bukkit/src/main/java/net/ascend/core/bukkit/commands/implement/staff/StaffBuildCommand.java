package net.ascend.core.bukkit.commands.implement.staff;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffBuildCommand extends BaseCommand {

    public StaffBuildCommand() {
        super("staffbuild", "xenon.command.staffbuild", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) {
            XenonBukkit.getInstance().getStaffManager().enableStaffBuild(player);
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-build.enabled")));
        } else {
            XenonBukkit.getInstance().getStaffManager().disableStaffBuild(player);
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-build.disabled")));
        }
    }
}
