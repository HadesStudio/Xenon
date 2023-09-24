package dev.hely.core.bukkit.profile.notifications.commands.impl;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class NotificationsAdminSendCommand extends SubCommand {
    public NotificationsAdminSendCommand() {
        super("send", Collections.singletonList("senduser"), "oxygen.notifications.admin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send.usage")));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        String type = String.join("\n", new String[]{ChatUtil.joinArray(args, " ", 2)});
        OxygenBukkit.getInstance().getProfileManager().sendNotification(profile.getUniqueId(), type);

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send.sent")));
    }
}

