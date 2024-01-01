package net.ascend.core.bukkit.staff.requests.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.staff.requests.commands
//   Date: Sunday, July 30, 2023 - 4:49 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.packet.implement.StaffRequestReportPacket;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends BaseCommand {

    public ReportCommand(){
        super("report", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage(CC.translate(Locale.REPORT_USAGE));
            return;
        }

        if (!Bukkit.getPlayer(args[0]).isOnline()) {
            player.sendMessage(CC.translate(Locale.REPORT_SELF));
            return;
        }

        if (args[0].equalsIgnoreCase(player.getName())) {
            player.sendMessage(CC.translate(Locale.REPORT_NOT_ONLINE));
            return;
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRequestReportPacket(
                player.getName(),
                XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"),
                args[1],
                args[0]));

        XenonBukkit.getInstance().getRequestHandler().createNewRequestReport(player.getName(), args[1], args[0]);
        player.sendMessage(CC.translate(Locale.REPORT_CREATED));
    }
}
