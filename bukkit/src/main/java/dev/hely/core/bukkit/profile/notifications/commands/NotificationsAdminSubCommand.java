package dev.hely.core.bukkit.profile.notifications.commands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.notifications.commands.impl.NotificationsAdminMenuCommand;
import dev.hely.core.bukkit.profile.notifications.commands.impl.NotificationsAdminSendAllCommand;
import dev.hely.core.bukkit.profile.notifications.commands.impl.NotificationsAdminSendCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommandHandler;
import dev.hely.lib.util.CC;
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
                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("notifications-admin.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
                return false;
            }
        } else {
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("notifications-admin.usage")) {
                sender.sendMessage(CC.translate(message));
            }
            return false;
        }
        return false;
    }
}
