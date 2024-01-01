package net.ascend.core.bukkit.profile.tfa.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.tfa.commands
//   Date: Thursday, August 10, 2023 - 5:32 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.tfa.utils.TFAUtil;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AuthCommand extends BaseCommand {

    public AuthCommand(){
        super("auth", Arrays.asList("2fa", "twofactor", "code", "authcode"), "xenon.staff", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        XenonBukkit.getInstance().getTaskUtil().run(() -> {
            Player player = (Player) sender;

            if (XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()) == null) {
                player.sendMessage(CC.translate("&cYour profile is null somehow? Please create a ticket in Hely Development discord."));
                return;
            }

            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (profile.isSettingUpAuth() && !profile.isNeedsVerify()) {
                XenonBukkit.getInstance().getTfaManager().giveQRMap(player);
                return;
            }

            if (!profile.isNeedsVerify()) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("auth-command.already-auth")));
                return;
            }

            if (args.length == 0) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("auth-command.usage")));
                return;
            }

            int tfaCode;
            try {
                tfaCode = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("auth-command.invalid-code")));
                return;
            }

            if (!TFAUtil.checkCode(profile.getTfaKey(), tfaCode)) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("auth-command.invalid-code")));
                return;
            }

            TFAUtil.removeQrMapFromInventory(player);
            profile.setSettingUpAuth(false);
            profile.setNeedsVerify(false);
            long next = TimeUtil.parseTime("5h");
            profile.setNextAuthenticationNeeded(next == -1 ? TimeUtil.parseTime("5h") + System.currentTimeMillis() : next + System.currentTimeMillis());
            XenonBukkit.getInstance().getTfaManager().giveItems(player);
        });
    }
}
