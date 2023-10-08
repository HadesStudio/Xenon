package dev.hely.core.bukkit.vip.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.vip.commands
//   Date: Saturday, September 16, 2023 - 3:10 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.core.bukkit.vip.menu.VIPMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VIPCommand extends BaseCommand {
    public VIPCommand() {
        super(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.settings.command-name"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        new VIPMenu(profile).openMenu(player);
    }
}
