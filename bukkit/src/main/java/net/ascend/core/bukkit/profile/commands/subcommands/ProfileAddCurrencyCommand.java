package net.ascend.core.bukkit.profile.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.LogType;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileAddCurrencyCommand extends SubCommand {

    public ProfileAddCurrencyCommand() {
        super("addcurrency", Collections.singletonList("addcurrency"), "xenon.profile.addcurrency");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.add-currency.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (!this.checkNumber(sender, args[1])) return;
        int amount = Math.abs(Integer.parseInt(args[1]));

        profile.setCurrency(profile.getCurrency() + amount);
        profile.save();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.add-currency.added").replace("%amount%", String.valueOf(amount)).replace("%profile%", profile.getName())));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            XenonBukkit.getInstance().getLogManager().logPlayerAction(player.getUniqueId(), player.getName(), Arrays.asList(
                    "&6Type&7: &cAdded Currency",
                    "&6Player&7: &c" + profile.getName(),
                    "&6Amount&7: &c" + amount
            ), LogType.STAFF_UPDATED_CURRENCY, LogCategories.CURRENCY_LOGS);

            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Type&7: &cAdded Currency",
                    "&6Staff Member&7: &c" + player.getName(),
                    "&6Amount&7: &c" + amount
            ), LogType.RECEIVED_CURRENCY, LogCategories.CURRENCY_LOGS);
        } else {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Type&7: &cAdded Currency",
                    "&6Staff Member&7: &4Console",
                    "&6Amount&7: &c" + amount
            ), LogType.RECEIVED_CURRENCY, LogCategories.CURRENCY_LOGS);
        }

        Player targetOnline = Bukkit.getPlayer(profile.getUniqueId());
        if (targetOnline != null && targetOnline.isOnline()) {
            targetOnline.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.add-currency.received").replace("%amount%", String.valueOf(amount)).replace("%sender%", sender.getName())));
        } else {
            List<String> list = XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("profile.send-notifications.add-currency");
            List<String> replacedList = list.stream().map(message -> message
                            .replace("%amount%", String.valueOf(amount)))
                    .collect(Collectors.toList());


            XenonBukkit.getInstance().getNotificationManager().sendNotification(profile.getUniqueId(),
                    replacedList,
                    XenonBukkit.getInstance().getNotificationManager().getRandomID());
        }
    }
}


