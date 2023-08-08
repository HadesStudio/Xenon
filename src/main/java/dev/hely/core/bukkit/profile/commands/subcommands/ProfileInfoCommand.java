package dev.hely.core.bukkit.profile.commands.subcommands;

import dev.hely.core.api.OxygenAPI;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
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
            sender.sendMessage(CC.translate("&cUsage: /profile info <player>"));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        sender.sendMessage(CC.translate(profile.getName() + "&e's Profile Information"));
        sender.sendMessage(CC.translate("&eUUID&7: &f" + profile.getUniqueId()));
        sender.sendMessage(CC.translate("&eCurrent Rank&7: &f" + OxygenAPI.INSTANCE.getRank(profile.getUniqueId())));
        sender.sendMessage(CC.translate("&eActive Grants&7: &f" + profile.getActiveGrants().size()));
        sender.sendMessage(CC.translate("&ePermissions&7: &f"));
        if (profile.getTag() == null || profile.getTag().isEmpty()) {
            sender.sendMessage(CC.translate("&eCurrent Tag&7: &fNone"));
        } else {
            sender.sendMessage(CC.translate("&eCurrent Tag&7: &f" + profile.getTag()));
        }
        sender.sendMessage(CC.translate("&eCurrent Friends&7: &f" + profile.getFriends().size()));

        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&eFirst Seen&7: &f" + TimeUtil.formatIntoCalendarString(new Date(profile.getFirstSeen()))));
        sender.sendMessage(CC.translate("&eLast Seen&7: &f" + TimeUtil.formatIntoCalendarString(new Date(profile.getLastSeen()))));
        sender.sendMessage(CC.translate("&eLast Server&7: &f" + profile.getLastServer()));
        sender.sendMessage(CC.translate("&eCurrency&7: &f" + profile.getCurrency()));

    }
}


