package net.ascend.core.bukkit.chat.commands.subcommands;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatMuteCommand extends SubCommand {

    public ChatMuteCommand() {
        super("mute", Collections.singletonList("mutechat"), "xenon.staff.chat.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (XenonBukkit.getInstance().getChatManager().isMuted()) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.mute.already-muted")));
            return;
        }
        XenonBukkit.getInstance().getChatManager().setMuted(true);
        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.mute.muted-chat")));
        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.mute.broadcast")) {
            sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}

