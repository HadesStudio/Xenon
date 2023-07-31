package dev.hely.core.staff.requests.commands;

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

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.packet.implement.StaffMessagePacket;
import dev.hely.core.profile.packet.implement.StaffRequestReportPacket;
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

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
            player.sendMessage(CC.translate(Locale.REPORT_NOT_ONLINE));
            return;
        }

        Oxygen.getInstance().getRedisModule().sendPacket(new StaffRequestReportPacket(
                player.getDisplayName(),
                Oxygen.getInstance().getServerManager().getServerName(),
                args[1],
                args[0]));

        Oxygen.getInstance().getRequestHandler().createNewRequestReport(player.getName(), args[1], args[0]);
        player.sendMessage(CC.translate(Locale.REPORT_CREATED));
    }
}
