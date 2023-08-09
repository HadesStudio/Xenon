package dev.hely.core.bukkit.chat.commands.subcommands;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatDonorTalkCommand extends SubCommand {

    public ChatDonorTalkCommand() {
        super("donortalk", Collections.singletonList("donorstatus"), "oxygen.staff.chat.donortalk");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (OxygenBukkit.getInstance().getChatManager().isAllow_high_rank_talk()) {
            OxygenBukkit.getInstance().getChatManager().setAllow_high_rank_talk(false);
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.donor-talk.enabled")));
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.donor-talk.enabled-broadcast")) {
                sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
            }
        } else {
            OxygenBukkit.getInstance().getChatManager().setAllow_high_rank_talk(true);
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.donor-talk.disabled")));
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.donor-talk.disabled-broadcast")) {
                sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
            }
        }
    }
}

