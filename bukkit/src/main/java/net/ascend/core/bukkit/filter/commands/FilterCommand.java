package net.ascend.core.bukkit.filter.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.filter.commands
//   Date: Thursday, September 14, 2023 - 2:12 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.filter.menu.FiltersMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FilterCommand extends BaseCommand {
    public FilterCommand(){
        super("filter", Arrays.asList("chatfilter", "filters"),"xenon.command.filter", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player senderPlayer = (Player) sender;

        new FiltersMenu().openMenu(senderPlayer);
    }
}
