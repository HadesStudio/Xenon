package dev.hely.core.bukkit.staff.requests.commands;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.packet.implement.StaffMessagePacket;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class HelpopCommand extends BaseCommand {

    public HelpopCommand(){
        super("helpop", Arrays.asList("request", "helpstaff", "stafftp"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate(Locale.HELPOP_USAGE));
            return;
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffMessagePacket(
                player.getDisplayName(),
                OxygenBukkit.getInstance().getServerManager().getServerName(),
                ChatUtil.joinArray(args, " ", 1)));

        OxygenBukkit.getInstance().getRequestHandler().createNewRequest(player.getName(), ChatUtil.joinArray(args, " ", 1));
        player.sendMessage(CC.translate(Locale.HELPOP_CREATED));
    }
}
