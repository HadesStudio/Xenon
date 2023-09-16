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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CouponAddCommand extends SubCommand {
    public CouponAddCommand() {
        super("add", Arrays.asList("give", "givecoupon"), "oxygen.store.coupon.add");
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
        if (JavaUtil.tryParseDouble(args[1]) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.coupons-command.add.invalid-amount")));
            return;
        }
        double amount = Double.parseDouble(args[1]);
        Coupon coupons = new Coupon(profile.getUniqueId(), "Gifted to you by: " + sender.getName(), amount);
        OxygenBukkit.getInstance().getCouponManager().getCoupons().add(coupons);

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.coupons-command.add.added").replace("%player%", profile.getName()).replace("%amount%", String.valueOf(amount))));
        if (Bukkit.getPlayer(profile.getName()).isOnline()) {
            Player player = Bukkit.getPlayer(profile.getName());
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("messages.commands.coupons-command.add.received").replace("%amount%", String.valueOf(amount))));
        }
    }
}

