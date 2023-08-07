package dev.hely.core.bukkit.server.command;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.command
//   Date: Sunday, August 06, 2023 - 8:03 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.menu.ServerMenu;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ServersCommand extends BaseCommand {

    public ServersCommand(){
        super("server", Collections.singletonList("servers"), "oxygen.staff.servers",true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (OxygenBukkit.getInstance().getServerManager().getServers().size() == 0) {
            player.sendMessage(CC.translate("&cSomehow, their is 0 servers currently online.. How are you on the server??"));
            return;
        }

        new ServerMenu().openMenu(player);
    }
}
