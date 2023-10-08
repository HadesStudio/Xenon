package dev.hely.core.bukkit.profile.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.commands.subcommands
//   Date: Saturday, September 02, 2023 - 12:08 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.permission.ProfilePermission;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ProfileRemovePermissionCommand extends SubCommand {
    public ProfileRemovePermissionCommand() {
        super("removeperm", Collections.singletonList("removepermission"), "oxygen.profile.removepermission");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.remove-permission.usage")));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        List<ProfilePermission> newPermList = profile.getProfilePermissions();
        for (ProfilePermission profilePermission : newPermList) {
            if (profilePermission.getPermission().equalsIgnoreCase(args[2])) {
                newPermList.remove(profilePermission);
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.remove-permission.removed").replace("%player%", profile.getName()).replace("%permission%", args[2])));
                return;
            }
        }
        profile.setProfilePermissions(newPermList);
        profile.save();
    }
}

