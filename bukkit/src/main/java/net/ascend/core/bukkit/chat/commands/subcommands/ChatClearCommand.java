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
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ChatClearCommand extends SubCommand {

    public ChatClearCommand() {
        super("clear", Collections.singletonList("clearchat"), "xenon.staff.chat.clear");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final StringBuilder sb = new StringBuilder();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("xenon.staff")) {
                continue;
            }
            for (int i = 0; i < 150; ++i) {
                player.sendMessage("\n");
            }
        }

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.clear.cleared")));
        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.clear.broadcast")) {
            sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}
