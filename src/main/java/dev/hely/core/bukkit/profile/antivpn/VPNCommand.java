package dev.hely.core.bukkit.profile.antivpn;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.antivpn.commands
//   Date: Wednesday, August 02, 2023 - 7:10 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.VPNUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.Collections;

public class VPNCommand extends BaseCommand {

    public VPNCommand(){
        super("vpn", Collections.singletonList("antivpn"), "oxygen.staff.antivpn", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("check")) {
                if (OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]) == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                    return;
                }
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);

                if (profile.isVpnWhitelist()) {
                    try {
                        long time = System.nanoTime();
                        VPNUtil.Result ip = VPNUtil.getResult(profile.getCurrentAddress());
                        double elapsed = (System.nanoTime() - time) / 1000000D;

                        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("vpn.check.whitelist")) {
                            sender.sendMessage(CC.translate(message)
                                    .replace("%player_name%", profile.getName())
                                    .replace("%country%", ip.getCountry())
                                    .replace("%asn%", ip.getAsn())
                                    .replace("%org%", ip.getOrg())
                                    .replace("%time_took%", String.valueOf(elapsed))
                            );
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        long time = System.nanoTime();
                        VPNUtil.Result ip = VPNUtil.getResult(profile.getCurrentAddress());
                        double elapsed = (System.nanoTime() - time) / 1000000D;

                        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("vpn.check.not-whitelist")) {
                            sender.sendMessage(CC.translate(message)
                                    .replace("%player_name%", profile.getName())
                                    .replace("%country%", ip.getCountry())
                                    .replace("%asn%", ip.getAsn())
                                    .replace("%org%", ip.getOrg())
                                    .replace("%time_took%", String.valueOf(elapsed))
                            );
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (args[0].equalsIgnoreCase("whitelist")) {
                if (OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]) == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                    return;
                }
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
                if (profile.isVpnWhitelist()) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vpn.whitelist.already-whitelist")));
                } else {
                    profile.setVpnWhitelist(true);
                    profile.save();
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vpn.whitelist.whitelisted")));
                }
            } else if (args[0].equalsIgnoreCase("unwhitelist")) {
                if (OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]) == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                    return;
                }
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);

                if (profile.isVpnWhitelist()) {
                    profile.setVpnWhitelist(false);
                    profile.save();
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vpn.unwhitelist.unwhitelisted")));
                } else {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vpn.unwhitelist.not-whitelist")));
                }
            } else {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vpn.usage")));
            }
        } else {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("vpn.usage")));
        }
    }
}
