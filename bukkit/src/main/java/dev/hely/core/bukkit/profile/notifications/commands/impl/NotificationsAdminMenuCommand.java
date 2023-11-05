package dev.hely.core.bukkit.profile.notifications.commands.impl;

import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.notifications.menu.NotificationListMenu;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class NotificationsAdminMenuCommand extends SubCommand {
    public NotificationsAdminMenuCommand() {
        super("menu", Collections.singletonList("utilmenu"), "oxygen.notifications.admin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            new NotificationListMenu().openMenu(player);
        } else{
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
        }
    }
}

