package net.ascend.core.bukkit.commands.implement.player;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.commands.implement.player
//   Date: Sunday, July 30, 2023 - 9:04 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.NameMCUtils;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class NameMCCommand extends BaseCommand {

    public NameMCCommand(){
        super("namemc", Collections.singletonList("vote"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (NameMCUtils.isVerified(player.getUniqueId())) {
            NameMCUtils.runCommands(player);
            for (Player players : Bukkit.getOnlinePlayers()) {
                for (String message : XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.messages.broadcast")) {
                    players.sendMessage(CC.translate(message).replace("%player%", player.getName()));
                }
            }
            for (String message : XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.messages.linked")) {
                player.sendMessage(CC.translate(message).replace("%player%", player.getName()));
            }
        } else {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("name-mc.messages.not-linked")));
        }
    }
}
