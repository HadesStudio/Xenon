package net.ascend.core.bukkit.server.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.commands
//   Date: Sunday, August 27, 2023 - 8:41 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.packet.implement.StaffRawMessagePacket;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SetSlotsCommand extends BaseCommand {
    public SetSlotsCommand(){
        super("slots", Collections.singletonList("setslots"), "xenon.command.whitelist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("slots-command.usage")));
            return;
        }

        if (!this.checkNumber(sender, args[0])) return;
        int amount = Math.abs(Integer.parseInt(args[0]));

        XenonBukkit.getInstance().getServerData().setMaxPlayers(amount);
        XenonBukkit.getInstance().getServerData().save();
        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("slots-command.updated").replace("%amount%", String.valueOf(amount))));
        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.admin", XenonBukkit.getInstance().getServerData().getName(), XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("slots-command.staff-broadcast").replace("%user%", sender.getName()).replace("%amount%", String.valueOf(amount))));
    }
}

