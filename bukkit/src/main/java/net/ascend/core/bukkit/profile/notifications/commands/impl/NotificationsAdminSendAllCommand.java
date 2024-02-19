package net.ascend.core.bukkit.profile.notifications.commands.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class NotificationsAdminSendAllCommand extends SubCommand {
    public NotificationsAdminSendAllCommand() {
        super("sendall", Collections.singletonList("sendallusers"), "xenon.notifications.admin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send-all.usage")));
            return;
        }

        String type = String.join("\n", new String[]{ChatUtil.joinArray(args, " ", 1)});
        XenonBukkit.getInstance().getProfileManager().sendNotificationAll(type);

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send-all.sent")));
    }
}

