package net.ascend.core.bukkit.profile.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.permission.ProfilePermission;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ProfileRemovePermissionCommand extends SubCommand {

    public ProfileRemovePermissionCommand() {
        super("removeperm", Collections.singletonList("removepermission"), "xenon.profile.removepermission");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.remove-permission.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        List<ProfilePermission> newPermList = profile.getProfilePermissions();
        for (ProfilePermission profilePermission : newPermList) {
            if (profilePermission.getPermission().equalsIgnoreCase(args[1])) {
                newPermList.remove(profilePermission);
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.remove-permission.removed")
                        .replace("%player%", profile.getName())
                        .replace("%permission%", args[1])));
                return;
            }
        }

        profile.setProfilePermissions(newPermList);
        profile.save();
    }
}

