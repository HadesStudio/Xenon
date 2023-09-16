package dev.hely.core.bukkit.coins.coupons.commands;

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
import dev.hely.core.bukkit.coins.coupons.commands.subcommands.CouponAddCommand;
import dev.hely.core.bukkit.coins.coupons.commands.subcommands.CouponRemoveCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommandHandler;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class CouponCommand extends SubCommandHandler {
    public CouponCommand() {
        super("coupon", Collections.singletonList("storecoupon"));

        this.addSubCommand(new CouponAddCommand());
        this.addSubCommand(new CouponRemoveCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.commands.coupons-command.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
            }
        } else {
                for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.commands.coupons-command.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
            }
        return false;
    }
}