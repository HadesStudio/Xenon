package net.ascend.core.bukkit.commands.implement.player;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.commands.implement.player
//   Date: Wednesday, August 02, 2023 - 7:53 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SeenCommand extends BaseCommand {

    public SeenCommand(){
        super("seen", Collections.singletonList("lastseen"),"xenon.seen");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("seen-command.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("seen-command.message")
                .replace("%player%", profile.getName())
                .replace("%time%", TimeUtil.formatDateDiff(profile.getLastSeen()))
                .replace("%server%", profile.getLastServer()))
        );
    }
}
