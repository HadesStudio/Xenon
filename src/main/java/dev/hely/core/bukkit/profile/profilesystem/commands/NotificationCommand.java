package dev.hely.core.bukkit.profile.profilesystem.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.commands
//   Date: Saturday, July 15, 2023 - 7:00 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

public class NotificationCommand extends BaseCommand {

    public NotificationCommand(){
        super("sendnotification","oxygen.notification");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Locale.HISTORY_USAGE));
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
        } else {
        }
    }
}
