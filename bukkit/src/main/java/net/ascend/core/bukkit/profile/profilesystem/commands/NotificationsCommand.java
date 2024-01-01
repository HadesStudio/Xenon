package net.ascend.core.bukkit.profile.profilesystem.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.commands
//   Date: Saturday, July 15, 2023 - 7:35 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.profilesystem.menu.type.NotificationsMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotificationsCommand extends BaseCommand {

    public NotificationsCommand(){
        super("notifications");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new NotificationsMenu(XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId())).openMenu(player);
    }
}
