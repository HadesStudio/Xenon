package net.ascend.core.bukkit.chat.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.chat.commands.subcommands
//   Date: Wednesday, August 09, 2023 - 3:40 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatDonorTalkCommand extends SubCommand {

    public ChatDonorTalkCommand() {
        super("donortalk", Collections.singletonList("donorstatus"), "xenon.staff.chat.donortalk");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (XenonBukkit.getInstance().getChatManager().isAllow_high_rank_talk()) {
            XenonBukkit.getInstance().getChatManager().setAllow_high_rank_talk(false);
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.donor-talk.enabled")));
            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.donor-talk.enabled-broadcast")) {
                sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
            }
        } else {
            XenonBukkit.getInstance().getChatManager().setAllow_high_rank_talk(true);
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.donor-talk.disabled")));
            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.donor-talk.disabled-broadcast")) {
                sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
            }
        }
    }
}

