package net.ascend.core.bukkit.vip.commands;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class VIPResetCommand extends BaseCommand {
    public VIPResetCommand() {
        super(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.settings.command-name") + "reset", "xenon.vipreset", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vip-reset.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }
        profile.setVipCooldown(0);
        profile.save();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vip-reset.reset").replace("%player%", profile.getName())));
        if (Bukkit.getPlayer(profile.getName()).isOnline()) {
            Bukkit.getPlayer(profile.getName()).sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vip-reset.message")));
        }

    }
}
