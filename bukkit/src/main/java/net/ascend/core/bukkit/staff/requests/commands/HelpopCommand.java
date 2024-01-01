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
import net.ascend.core.bukkit.profile.packet.implement.StaffMessagePacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
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

        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffMessagePacket(
                player.getName(),
                XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"),
                ChatUtil.joinArray(args, " ", 1)));

        XenonBukkit.getInstance().getRequestHandler().createNewRequest(player.getName(), ChatUtil.joinArray(args, " ", 1));
        player.sendMessage(CC.translate(Locale.HELPOP_CREATED));
    }
}
