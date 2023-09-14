package dev.hely.core.bukkit.server.commands;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.packet.implement.StaffRawMessagePacket;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SetSlotsCommand extends BaseCommand {
    public SetSlotsCommand(){
        super("slots", Collections.singletonList("setslots"), "oxygen.command.whitelist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("slots-command.usage")));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_INVALID_NUMBER));
            return;
        }

        OxygenBukkit.getInstance().getServerData().setMaxPlayers(amount);
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("slots-command.updated").replace("%amount%", String.valueOf(amount))));
        OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.admin", OxygenBukkit.getInstance().getServerData().getName(), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("slots-command.staff-broadcast").replace("%user%", sender.getName()).replace("%amount%", String.valueOf(amount))));
    }
}

