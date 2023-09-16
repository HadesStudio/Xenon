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
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatMuteCommand extends SubCommand {

    public ChatMuteCommand() {
        super("mute", Collections.singletonList("mutechat"), "oxygen.staff.chat.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (OxygenBukkit.getInstance().getChatManager().isMuted()) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.mute.already-muted")));
            return;
        }
        OxygenBukkit.getInstance().getChatManager().setMuted(true);
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.mute.muted-chat")));
        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.mute.broadcast")) {
            sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}

