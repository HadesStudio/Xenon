package dev.hely.core.bukkit.coins.sales.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.sales.commands.subcommands
//   Date: Monday, August 07, 2023 - 6:05 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.sales.types.SaleTypes;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class SaleEndCommand extends SubCommand {
    public SaleEndCommand() {
        super("end", Arrays.asList("cancel", "stop", "disable"), "oxygen.store.sale.end");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.commands.sale-command.usage")) {
                sender.sendMessage(CC.translate(message));
            }
            return;
        }
        if (SaleTypes.getByName(args[1]) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.sale-command.invalid-type")));
            return;
        }

        OxygenBukkit.getInstance().getSaleManager().getSales().remove(SaleTypes.getByName(args[1]));
    }
}

