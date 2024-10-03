package net.ascend.core.bukkit.profile.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.permission.ProfilePermission;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ProfileAddPermissionCommand extends SubCommand {

    public ProfileAddPermissionCommand() {
        super("addperm", Collections.singletonList("addpermission"), "xenon.profile.addpermission");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.add-permission.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        for (ProfilePermission profilePermission : profile.getProfilePermissions()) {
            if (profilePermission.getPermission().equalsIgnoreCase(args[1])) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.add-permission.already-has").replace("%player%", profile.getName())));
                return;
            }
        }

        String server = args[2];
        if (XenonBukkit.getInstance().getServerManager().getServer(server) == null && !server.equalsIgnoreCase("Global")) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.add-permission.invalid-server")));
            return;
        }

        long duration = -1;
        if (args.length >= 4) {
            try {
                duration = TimeUtil.parse(args[3]);
            } catch (NumberFormatException e) {
                return;
            }
        }

        boolean global = server.equalsIgnoreCase("Global");


        ProfilePermission permission = new ProfilePermission(args[1], args[2], global, duration, System.currentTimeMillis());
        profile.getProfilePermissions().add(permission);
        profile.save();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.add-permission.added")
                .replace("%player%", profile.getName())
                .replace("%permission%", args[1])));
    }
}


