package dev.hely.core.bukkit.profile.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.log.category.LogCategories;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-currency.usage")));
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
            sender.sendMessage(CC.translate(Locale.INVALID_INTEGER));
            return;
        }

        profile.setCurrency(profile.getCurrency() + amount);
        profile.save();

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-currency.added").replace("%amount%", String.valueOf(amount)).replace("%profile%", profile.getName())));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            OxygenBukkit.getInstance().getLogManager().logPlayerAction(player.getUniqueId(), player.getName(), Arrays.asList(
                    "&6Type&7: &aAdded Currency",
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount
            ), LogActionTypes.STAFF_UPDATED_CURRENCY, LogCategories.CURRENCY_LOGS);

            OxygenBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Type&7: &aAdded Currency",
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount
            ), LogActionTypes.RECEIVED_CURRENCY, LogCategories.CURRENCY_LOGS);
        } else {
            OxygenBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Type&7: &aAdded Currency",
                    "&6Staff Member&7: &4Console",
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount
            ), LogActionTypes.RECEIVED_CURRENCY, LogCategories.CURRENCY_LOGS);
        }

        if (Bukkit.getPlayer(profile.getName()).isOnline()) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-currency.received").replace("%amount%", String.valueOf(amount)).replace("%sender%", sender.getName())));
        } else {
            List<String> list = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-commands.add-currency.notification");
            OxygenBukkit.getInstance().getProfileManager().sendNotification(profile.getUniqueId(), list);
        }
    }
}


