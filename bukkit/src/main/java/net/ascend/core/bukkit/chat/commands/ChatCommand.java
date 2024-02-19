package net.ascend.core.bukkit.chat.commands;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.chat.commands.subcommands.*;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import net.ascend.core.bukkit.lib.CC;
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
                for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
            }
        } else {
            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.usage")) {
                sender.sendMessage(CC.translate(message));
            }
        }
        return false;
    }
}