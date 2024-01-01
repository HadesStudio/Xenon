package net.ascend.core.bukkit.profile.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.types.LogActionTypes;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 07/08/2023
 * Protect Name Xenon
 */

public class ProfileRemoveCurrencyCommand extends SubCommand {
    public ProfileRemoveCurrencyCommand() {
        super("removecurrency", Collections.singletonList("removecurrencys"), "xenon.profile.removecurrency");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.remove-currency.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_INVALID_NUMBER));
            return;
        }

        int currentCurrency = profile.getCurrency();
        if (currentCurrency < amount) { // dont want it to go negative :( puta
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.remove-currency.invalid")));
            return;
        }

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.remove-currency.removed").replace("%amount%", String.valueOf(amount)).replace("%profile%", profile.getName())));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            XenonBukkit.getInstance().getLogManager().logPlayerAction(player.getUniqueId(), player.getName(), Arrays.asList(
                    "&6Type&7: &aRemoved Currency",
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount,
                    "&6New Currency&7: &a" + (profile.getCurrency() - amount)
            ), LogActionTypes.STAFF_UPDATED_CURRENCY, LogCategories.CURRENCY_LOGS);

            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Type&7: &aRemoved Currency",
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount,
                    "&6New Currency&7: &a" + (profile.getCurrency() - amount)

            ), LogActionTypes.REMOVED_CURRENCY, LogCategories.CURRENCY_LOGS);
        } else {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Type&7: &aRemoved Currency",
                    "&6Staff Member&7: &4Console",
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount,
                    "&6New Currency&7: &a" + (profile.getCurrency() - amount)
            ), LogActionTypes.REMOVED_CURRENCY, LogCategories.CURRENCY_LOGS);
        }

        profile.setCurrency(profile.getCurrency() - amount);
        profile.save();

        if (Bukkit.getPlayer(profile.getName()).isOnline()) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.remove-currency.taken").replace("%amount%", String.valueOf(amount)).replace("%sender%", sender.getName())));
        } else {
            List<String> list = XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-commands.remove-currency.notification");
            XenonBukkit.getInstance().getProfileManager().sendNotification(profile.getUniqueId(), list, XenonBukkit.getInstance().getNotificationManager().getRandomID());
        }
    }
}


