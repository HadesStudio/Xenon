package dev.hely.core.bukkit.profile.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Date;

/**
 * Created By LeandroSSJ
 * Created on 07/08/2023
 * Protect Name Oxygen
 */

public class ProfileInfoCommand extends SubCommand {
    public ProfileInfoCommand() {
        super("info", Collections.singletonList("check"), "oxygen.profile.info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.info.usage")));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-commands.info.command")) {
            sender.sendMessage(CC.translate(message)
                    .replace("%player%", profile.getName())
                    .replace("%uuid%", profile.getUniqueId().toString())
                    .replace("%current_rank%", profile.getActiveHighestRank().getName())
                    .replace("%active_grants%", String.valueOf(profile.getActiveGrants().size()))
                    .replace("%profile_permissions_size%", String.valueOf(profile.getProfilePermissions().size()))
                    .replace("%bungee_permissions_size%", String.valueOf(profile.getBungeePermissions().size()))
                    .replace("%player_permissions_size%", String.valueOf(profile.getPlayerPermissions().size()))
                    .replace("%tag%", profile.getTagFormated())
                    .replace("%friends_size%", String.valueOf(profile.getFriends().size()))
                    .replace("%first_seen%", TimeUtil.formatIntoCalendarString(new Date(profile.getFirstSeen())))
                    .replace("%last_seen%", TimeUtil.formatIntoCalendarString(new Date(profile.getLastSeen())))
                    .replace("%last_server%", profile.getLastServer())
                    .replace("%currency_amount%", String.valueOf(profile.getCurrency()))
            );
        }
    }
}


