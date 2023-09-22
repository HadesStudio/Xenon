package dev.hely.core.bukkit.profile.profilesystem.commands.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.commands.impl
//   Date: Saturday, July 15, 2023 - 6:22 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendAddCommand extends SubCommand {
    public FriendAddCommand() {
        super("add", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player sender_player = (Player) sender;
            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                return;
            }
            if (profile.getName().equalsIgnoreCase(sender_player.getName())) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfig().getString("friends.friend-add.friend-self")));
                return;
            }

            OxygenBukkit.getInstance().getProfileManager().sendFriendRequest(sender_player, profile.getUniqueId());
        } else {
            sender.sendMessage(CC.translate("&cCorrect Usage: /friends add <player>"));
        }
    }
}
