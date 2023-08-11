package dev.hely.core.bukkit.chat.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.chat.commands
//   Date: Wednesday, August 09, 2023 - 3:35 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.chat.commands.subcommands.*;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommandHandler;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatCommand extends SubCommandHandler {
    public ChatCommand() {
        super("chat", Collections.singletonList("serverchat"));
        this.addSubCommand(new ChatMuteCommand());
        this.addSubCommand(new ChatUnmuteCommand());
        this.addSubCommand(new ChatDonorTalkCommand());
        this.addSubCommand(new ChatClearCommand());
        this.addSubCommand(new ChatSlowCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
            }
        } else {
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.usage")) {
                sender.sendMessage(CC.translate(message));
            }
        }
        return false;
    }
}