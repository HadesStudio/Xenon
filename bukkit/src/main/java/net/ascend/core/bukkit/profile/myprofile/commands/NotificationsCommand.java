package net.ascend.core.bukkit.profile.myprofile.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.myprofile.menus.type.NotificationsMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotificationsCommand extends BaseCommand {

    public NotificationsCommand(){
        super("notifications", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new NotificationsMenu(XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId())).openMenu(player);
    }
}
