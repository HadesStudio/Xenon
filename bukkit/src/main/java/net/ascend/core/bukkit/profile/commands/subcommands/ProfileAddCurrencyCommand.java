package net.ascend.core.bukkit.profile.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.types.LogActionTypes;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
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

public class ProfileAddCurrencyCommand extends SubCommand {
    public ProfileAddCurrencyCommand() {
        super("addcurrency", Collections.singletonList("addcurrency"), "xenon.profile.addcurrency");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-currency.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        if (!this.checkNumber(sender, args[2])) return;
        int amount = Math.abs(Integer.parseInt(args[2]));

        profile.setCurrency(profile.getCurrency() + amount);
        profile.save();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-currency.added").replace("%amount%", String.valueOf(amount)).replace("%profile%", profile.getName())));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            XenonBukkit.getInstance().getLogManager().logPlayerAction(player.getUniqueId(), player.getName(), Arrays.asList(
                    "&6Type&7: &aAdded Currency",
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount
            ), LogActionTypes.STAFF_UPDATED_CURRENCY, LogCategories.CURRENCY_LOGS);

            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Type&7: &aAdded Currency",
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount
            ), LogActionTypes.RECEIVED_CURRENCY, LogCategories.CURRENCY_LOGS);
        } else {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Type&7: &aAdded Currency",
                    "&6Staff Member&7: &4Console",
                    "&6Player&7: &a" + profile.getName(),
                    "&6Amount&7: &a" + amount
            ), LogActionTypes.RECEIVED_CURRENCY, LogCategories.CURRENCY_LOGS);
        }

        if (Bukkit.getPlayer(profile.getName()).isOnline()) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-currency.received").replace("%amount%", String.valueOf(amount)).replace("%sender%", sender.getName())));
        } else {
            List<String> list = XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-commands.add-currency.notification");
            XenonBukkit.getInstance().getProfileManager().sendNotification(profile.getUniqueId(), list, XenonBukkit.getInstance().getNotificationManager().getRandomID());
        }
    }
}


