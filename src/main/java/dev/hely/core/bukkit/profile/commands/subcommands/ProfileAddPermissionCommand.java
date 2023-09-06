package dev.hely.core.bukkit.profile.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.permission.ProfilePermission;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/08/2023
 * Protect Name Oxygen
 */

public class ProfileAddPermissionCommand extends SubCommand {

    public ProfileAddPermissionCommand() {
        super("addperm", Collections.singletonList("addpermission"), "oxygen.profile.addpermission");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-permission.usage")));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }

        for (ProfilePermission profilePermission : profile.getProfilePermissions()) {
            if (profilePermission.getPermission().equalsIgnoreCase(args[2])) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-permission.already-has").replace("%player%", profile.getName())));
                return;
            }
        }

        if (OxygenBukkit.getInstance().getServerManager().getOrSetupServer(args[3]) == null) {
            if (!args[3].equalsIgnoreCase("Global")) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-permission.invalid-server")));
                return;
            }
        }

        ProfilePermission permission = new ProfilePermission(args[2], args[3], false, -1);
        profile.getProfilePermissions().add(permission);
        profile.save();
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-commands.add-permission.added").replace("%player%", profile.getName()).replace("%permission%", args[2])));
    }
}


