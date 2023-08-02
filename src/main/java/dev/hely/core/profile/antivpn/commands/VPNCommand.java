package dev.hely.core.profile.antivpn.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.antivpn.commands
//   Date: Wednesday, August 02, 2023 - 7:10 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.utils.NameMCUtils;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class VPNCommand extends BaseCommand {

    public VPNCommand(){
        super("vpn", Collections.singletonList("antivpn"), "oxygen.staff.antivpn", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (NameMCUtils.isVerified(player.getUniqueId())) {
            NameMCUtils.runCommands(player);
            for (Player players : Bukkit.getOnlinePlayers()) {
                for (String message : Oxygen.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.messages.broadcast")) {
                    players.sendMessage(CC.translate(message).replace("%player%", player.getName()));
                }
            }
            for (String message : Oxygen.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.messages.linked")) {
                player.sendMessage(CC.translate(message).replace("%player%", player.getName()));
            }
        } else {
            player.sendMessage(CC.translate(Oxygen.getInstance().getConfiguration().getConfiguration().getString("name-mc.messages.not-linked")));
        }
    }
}
