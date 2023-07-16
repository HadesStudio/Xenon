package dev.hely.core.profile.profilesystem.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.commands
//   Date: Saturday, July 15, 2023 - 6:19 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.profile.profilesystem.commands.impl.FriendAddCommand;
import dev.hely.core.profile.profilesystem.menu.type.FriendsMenu;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.core.utils.command.sub.SubCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class FriendsCommand extends SubCommandHandler {
    public FriendsCommand() {
        super("friend", Collections.singletonList("friends"));

        this.addSubCommand(new FriendAddCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                Player player = (Player) sender;
                new FriendsMenu(Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId())).openMenu(player);
                return false;
            }
        } else {
            Player player = (Player) sender;
            new FriendsMenu(Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId())).openMenu(player);
            return false;
        }
        return false;
    }
}
