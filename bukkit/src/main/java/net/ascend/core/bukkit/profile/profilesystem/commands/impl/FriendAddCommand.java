package net.ascend.core.bukkit.profile.profilesystem.commands.impl;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
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
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                return;
            }
            if (profile.getName().equalsIgnoreCase(sender_player.getName())) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfig().getString("friends.friend-add.friend-self")));
                return;
            }

            XenonBukkit.getInstance().getProfileManager().sendFriendRequest(sender_player, profile.getUniqueId());
        } else {
            sender.sendMessage(CC.translate("&cCorrect Usage: /friends add <player>"));
        }
    }
}
