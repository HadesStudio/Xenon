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
import dev.hely.core.bukkit.coins.item.type.ItemTypes;
import dev.hely.core.bukkit.coins.sales.Sale;
import dev.hely.core.bukkit.coins.sales.SaleManager;
import dev.hely.core.bukkit.coins.sales.types.SaleTypes;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SaleStartCommand extends SubCommand {
    public SaleStartCommand() {
        super("start", Collections.singletonList("create"), "oxygen.store.sale.start");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.commands.sale-command.usage")) {
                sender.sendMessage(CC.translate(message));
            }
            return;
        }
        if (SaleTypes.getByName(args[1]) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.sale-command.invalid-type")));
            return;
        }
        long time = TimeUtil.parseTime(args[2]);
        Sale sale = new Sale(Integer.parseInt(args[3]), ItemTypes.ITEM, System.currentTimeMillis() + time, SaleTypes.getByName(args[1]));

        SaleManager.getSales().put(SaleTypes.getByName(args[1]), sale);

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.sale-command.started")
                .replace("%type%", String.valueOf(SaleTypes.getByName(args[1])))
                .replace("%time%", String.valueOf(time))
        ));
        for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.commands.sale-command.broadcast")) {
            sender.sendMessage(CC.translate(message)
                    .replace("%type%", String.valueOf(SaleTypes.getByName(args[1])))
                    .replace("%time%", String.valueOf(time))
            );
        }
    }
}
