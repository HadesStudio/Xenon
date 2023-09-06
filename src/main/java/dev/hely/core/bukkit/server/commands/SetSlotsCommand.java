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

import dev.hely.core.bukkit.utils.command.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SetSlotsCommand extends BaseCommand {

    public SetSlotsCommand(){
        super("slots", Collections.singletonList("setslots"), "oxygen.command.whitelist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            //sender.sendMessage(CC.RED + "Usage: /slots <slots>");
            return;
        }

    }
}

