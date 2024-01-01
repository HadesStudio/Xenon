package net.ascend.core.bukkit.profile.notifications.commands.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class NotificationsAdminSendCommand extends SubCommand {
    public NotificationsAdminSendCommand() {
        super("send", Collections.singletonList("senduser"), "xenon.notifications.admin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        String type = String.join("\n", new String[]{ChatUtil.joinArray(args, " ", 2)});
        XenonBukkit.getInstance().getProfileManager().sendNotification(profile.getUniqueId(), type);

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send.sent")));
    }
}

