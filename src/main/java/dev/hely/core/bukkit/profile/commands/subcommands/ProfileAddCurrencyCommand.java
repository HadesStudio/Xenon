package dev.hely.core.bukkit.profile.commands.subcommands;

import dev.hely.core.api.OxygenAPI;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Date;

/**
 * Created By LeandroSSJ
 * Created on 07/08/2023
 * Protect Name Oxygen
 */

public class ProfileAddCurrencyCommand extends SubCommand {

    public ProfileAddCurrencyCommand() {
        super("addcurrency", Collections.singletonList("addcurrency"), "oxygen.profile.addcurrency");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cUsage: /profile addcurrency <player> <amount>"));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cYou have provided a invalid integer amount."));
            return;
        }

        profile.setCurrency(profile.getCurrency() + amount);
        profile.save();

        sender.sendMessage(CC.translate("&cYou have successfully added &6" + amount + " Coins &cto &c&l" + profile.getName() + "'s Profile&c."));
        if (Bukkit.getPlayer(profile.getName()).isOnline()) {
            Bukkit.getPlayer(profile.getUniqueId()).sendMessage(CC.translate("&6[Profile] &c&l" + sender.getName()) + " &chas just given you &6" + amount + " Coins&c.");
        }

    }
}


