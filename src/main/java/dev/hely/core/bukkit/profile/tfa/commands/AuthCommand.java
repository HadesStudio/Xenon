package dev.hely.core.bukkit.profile.tfa.commands;

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

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.tfa.utils.TFAQRGenerator;
import dev.hely.core.bukkit.profile.tfa.utils.TFAUtil;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class AuthCommand extends BaseCommand {
    public AuthCommand(){
        super("auth", Arrays.asList("2fa", "twofactor", "code", "authcode"), "oxygen.staff", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OxygenBukkit.getInstance().getTasks().run(() -> {
            Player player = (Player) sender;
            if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()) == null) {
                player.sendMessage(CC.translate("&cYour profile is null somehow? Please create a ticket in Hely Development discord."));
                return;
            }

            final Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (profile.isSettingUpAuth() && !profile.isNeedsVerify()) {
                OxygenBukkit.getInstance().getTfaManager().giveQRMap(player);
                return;
            }
            if (!profile.isNeedsVerify()) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("auth-command.already-auth")));
                return;
            }
            if (args.length == 0) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("auth-command.usage")));
                return;
            }
            int tfaCode;
            try {
                tfaCode = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("auth-command.invalid-code")));
                return;
            }
            if (!TFAUtil.checkCode(profile.getTfaKey(), tfaCode)) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("auth-command.invalid-code")));
                return;
            }
            TFAUtil.removeQrMapFromInventory(player);
            profile.setSettingUpAuth(false);
            profile.setNeedsVerify(false);
            long next = TimeUtil.parseTime("5h");
            profile.setNextAuthenticationNeeded(next == -1 ? TimeUtil.parseTime("5h") + System.currentTimeMillis() : next + System.currentTimeMillis());
            OxygenBukkit.getInstance().getTfaManager().clearItems(player);
        });
    }
}
