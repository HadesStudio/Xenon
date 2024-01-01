package net.ascend.core.bukkit.profile.notifications.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.notifications.commands.impl.NotificationsAdminMenuCommand;
import net.ascend.core.bukkit.profile.notifications.commands.impl.NotificationsAdminSendAllCommand;
import net.ascend.core.bukkit.profile.notifications.commands.impl.NotificationsAdminSendCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class NotificationsAdminSubCommand extends SubCommandHandler {

    public NotificationsAdminSubCommand() {
        super("notificationsadmin", Collections.singletonList("notiadmin"));

        this.addSubCommand(new NotificationsAdminSendCommand());
        this.addSubCommand(new NotificationsAdminSendAllCommand());
        this.addSubCommand(new NotificationsAdminMenuCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("notifications-admin.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
                return false;
            }
        } else {
            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("notifications-admin.usage")) {
                sender.sendMessage(CC.translate(message));
            }
            return false;
        }
        return false;
    }
}
