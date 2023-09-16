package dev.hely.core.bukkit.chat.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.chat.commands.subcommands
//   Date: Wednesday, August 09, 2023 - 3:39 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatSlowCommand extends SubCommand {
    public ChatSlowCommand() {
        super("slow", Collections.singletonList("slowchat"), "oxygen.staff.chat.slow");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.slow-chat.usage")));
            return;
        }
        int time;

        try {
            time = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.slow-chat.invalid-time")));
            return;
        }
        if (time == 0) {
            OxygenBukkit.getInstance().getChatManager().setSlowTime(0);
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.slow-chat.disabled-slow")));
            return;
        }

        OxygenBukkit.getInstance().getChatManager().setSlowTime(time);
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.slow-chat.slowed-chat").replace("%time%", String.valueOf(time))));
        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.slow-chat.broadcast")) {
            Bukkit.broadcastMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}