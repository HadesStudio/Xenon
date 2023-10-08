package dev.hely.core.bukkit.profile.notifications.commands.impl;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class NotificationsAdminSendAllCommand extends SubCommand {
    public NotificationsAdminSendAllCommand() {
        super("sendall", Collections.singletonList("sendallusers"), "oxygen.notifications.admin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send-all.usage")));
            return;
        }

        String type = String.join("\n", new String[]{ChatUtil.joinArray(args, " ", 1)});
        OxygenBukkit.getInstance().getProfileManager().sendNotificationAll(type);

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send-all.sent")));
    }
}

