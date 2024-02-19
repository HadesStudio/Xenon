package net.ascend.core.bukkit.profile.notifications.commands.impl;

import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.notifications.menu.NotificationListMenu;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class NotificationsAdminMenuCommand extends SubCommand {
    public NotificationsAdminMenuCommand() {
        super("menu", Collections.singletonList("utilmenu"), "xenon.notifications.admin");
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

