package net.ascend.core.bukkit.commands.implement.player.toggle;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.player
//   Date: Wednesday, August 09, 2023 - 8:39 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ToggleSoundsCommand extends BaseCommand {
    public ToggleSoundsCommand(){
        super("togglesounds", Arrays.asList("messagesounds", "sounds", "togglemessagesounds"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile == null) {
            sender.sendMessage(CC.translate("&cSomehow your profile is null, please open a ticket in the Hely Development discord."));
            return;
        }

        if (profile.getSettings().isPlayingMessageSounds()) {
            profile.getSettings().setPlayingMessageSounds(false);
            profile.save();
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("togglesounds-command.disabled")));
        } else {
            profile.getSettings().setPlayingMessageSounds(true);
            profile.save();
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("togglesounds-command.enabled")));
        }
    }
}

