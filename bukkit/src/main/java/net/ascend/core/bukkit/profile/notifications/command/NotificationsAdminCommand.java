package net.ascend.core.bukkit.profile.notifications.command;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.notifications.menu.NotificationAdminMenu;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class NotificationsAdminCommand extends BaseCommand {

    public NotificationsAdminCommand() {
        super("notificationsadmin", Collections.singletonList("notiadmin"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("notifications-admin.usage")) {
                sender.sendMessage(CC.translate(message));
            }
            return;
        }

        switch (args[0].toLowerCase()) {
            case "menu":
                executeMenuCommand(sender, args);
                break;
            case "sendall":
                executeSendAllCommand(sender, args);
                break;
            case "send":
                executeSendCommand(sender, args);
                break;
            default:
                for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("notifications-admin.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
                break;
        }
    }

    private void executeMenuCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }

        Player player = (Player) sender;
        new NotificationAdminMenu().openMenu(player);
    }

    private void executeSendAllCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send-all.usage")));
            return;
        }

        String type = String.join("\n", new String[]{ ChatUtil.joinArray(args, " ", 1)});
        XenonBukkit.getInstance().getNotificationManager().sendNotificationAll(type);

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send-all.sent")));
    }

    private void executeSendCommand(CommandSender sender, String[] args) {
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
        XenonBukkit.getInstance().getNotificationManager().sendNotification(profile.getUniqueId(), type);

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notifications-admin.send.sent")));

    }
}
