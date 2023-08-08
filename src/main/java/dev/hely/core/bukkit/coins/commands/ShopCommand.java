package dev.hely.core.bukkit.coins.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.commands
//   Date: Monday, August 07, 2023 - 8:09 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.coins.menu.CustomMenu;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ShopCommand extends BaseCommand {

    public ShopCommand(){
        super("shop", Arrays.asList("coinshop", "store", "coinstore", "buy"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new CustomMenu().openMenu(player);
    }
}
