package net.ascend.core.bukkit.profile.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Date;

/**
 * Created By LeandroSSJ
 * Created on 07/08/2023
 * Protect Name Xenon
 */

public class ProfileInfoCommand extends SubCommand {
    public ProfileInfoCommand() {
        super("info", Collections.singletonList("check"), "xenon.profile.info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.info.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-commands.info.command")) {
            sender.sendMessage(CC.translate(message)
                    .replace("%player%", profile.getName())
                    .replace("%uuid%", profile.getUniqueId().toString())
                    .replace("%current_rank%", profile.getActiveRank().getName())
                    .replace("%active_grants%", String.valueOf(profile.getGrants().size()))
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


