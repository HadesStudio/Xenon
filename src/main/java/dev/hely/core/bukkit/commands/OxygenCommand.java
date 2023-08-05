package dev.hely.core.bukkit.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.core.commands
//   Date: Saturday, August 05, 2023 - 1:58 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.core.menus.OxygenMenu;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OxygenCommand extends BaseCommand {
    private final List<UUID> uuidList = Arrays.asList(
            UUID.fromString("d0ba628c-4d44-4b3b-aba9-4f62979ff303"), // LEANDROSSJ
            UUID.fromString("c1f714e6-1ba1-45c3-ad0f-aee338d6c822")); //DevDipin

    public OxygenCommand(){
        super("oxygen", Collections.singletonList("core"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            for (UUID admin : uuidList) {
                Player player = (Player) sender;
                if (admin.equals(player.getUniqueId())) {
                    new OxygenMenu(player).openMenu(player);
                }
            }
        }
    }
}
