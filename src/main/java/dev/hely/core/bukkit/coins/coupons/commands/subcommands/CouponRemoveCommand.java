package dev.hely.core.bukkit.coins.coupons.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.coupons.commands.subcommands
//   Date: Monday, September 04, 2023 - 9:11 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.coupons.Coupon;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import dev.hely.lib.JavaUtil;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CouponRemoveCommand extends SubCommand {
    public CouponRemoveCommand() {
        super("remove", Arrays.asList("removecoupon", "delete"), "oxygen.store.coupon.remove");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.commands.coupons-command.usage")) {
                sender.sendMessage(CC.translate(message));
            }
            return;
        }
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }
        if (JavaUtil.tryParseInt(args[1]) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.coupons-command.remove.invalid-coupon")));
            return;
        }
        int id = Integer.parseInt(args[1]);
        if (OxygenBukkit.getInstance().getCouponManager().getCouponById(id) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.coupons-command.remove.invalid-coupon")));
            return;
        }
        List<Coupon> coupon = OxygenBukkit.getInstance().getCouponManager().getCouponById(id);
        for (Coupon coupons : coupon) {
            OxygenBukkit.getInstance().getCouponManager().getCoupons().remove(coupons);
        }

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.coupons-command.remove.removed")));
    }
}

