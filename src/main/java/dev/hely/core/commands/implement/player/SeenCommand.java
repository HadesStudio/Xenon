package dev.hely.core.commands.implement.player;

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

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.menu.EditProfileMenu;
import dev.hely.core.utils.DateUtils;
import dev.hely.core.utils.TimeUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SeenCommand extends BaseCommand {

    public SeenCommand(){
        super("seen", Collections.singletonList("lastseen"),"oxygen.seen");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("seen-command.usage")));
            return;
        }

        if (Oxygen.getInstance().getProfileManager().getProfile(args[0]) == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[0]);

        sender.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("seen-command.message")
                .replace("%player%", profile.getName())
                .replace("%time%", DateUtils.formatDateDiff(profile.getLastSeen()))
                .replace("%server%", profile.getLastServer()))
        );
    }
}
