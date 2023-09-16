package dev.hely.core.bukkit.coins.sales.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.sales.commands
//   Date: Monday, August 07, 2023 - 5:49 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.sales.commands.subcommands.SaleEndCommand;
import dev.hely.core.bukkit.coins.sales.commands.subcommands.SaleStartCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommandHandler;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class StoreSaleCommand extends SubCommandHandler {
    public StoreSaleCommand() {
        super("sale", Collections.singletonList("storesale"));

        this.addSubCommand(new SaleStartCommand());
        this.addSubCommand(new SaleEndCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.commands.sale-command.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
            }
        } else {
                for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.commands.sale-command.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
            }
        return false;
    }
}