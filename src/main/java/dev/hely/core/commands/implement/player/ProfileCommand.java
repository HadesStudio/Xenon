package dev.hely.core.commands.implement.player;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.commands.implement.player
//   Date: Saturday, July 15, 2023 - 4:54 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.packet.implement.AdminChatPacket;
import dev.hely.core.profile.profilesystem.menu.MainProfileMenu;
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ProfileCommand extends BaseCommand {

    public ProfileCommand(){
        super("profile", Collections.singletonList("p"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId());

        new MainProfileMenu(profile).openMenu(player);
    }
}
