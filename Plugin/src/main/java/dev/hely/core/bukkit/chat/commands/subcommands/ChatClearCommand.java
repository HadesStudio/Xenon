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
import org.bukkit.entity.Player;

import java.util.Collections;

public class ChatClearCommand extends SubCommand {

    public ChatClearCommand() {
        super("clear", Collections.singletonList("clearchat"), "oxygen.staff.chat.clear");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final StringBuilder sb = new StringBuilder();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("oxygen.staff")) {
                continue;
            }
            for (int i = 0; i < 150; ++i) {
                player.sendMessage("\n");
            }
        }

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.clear.cleared")));
        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.clear.broadcast")) {
            sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}
