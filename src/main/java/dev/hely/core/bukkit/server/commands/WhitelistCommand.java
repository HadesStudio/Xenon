package dev.hely.core.bukkit.server.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.commands
//   Date: Sunday, August 27, 2023 - 8:08 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.packet.implement.WhitelistBroadcastPacket;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.server.data.server.data.StatusData;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class WhitelistCommand extends BaseCommand {
    public WhitelistCommand(){
        super("whitelist", Collections.singletonList("wl"), "oxygen.command.whitelist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.usage")));
        } else if (args[0].equalsIgnoreCase("on")) {
            Server server = OxygenBukkit.getInstance().getServerManager().serverFromName(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
            if (server == null) {
                sender.sendMessage(CC.translate("&cTheir was an error executing this command. (Error #1)"));
                return;
            }
            if (server.getStatus() == StatusData.WHITELISTED) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.already-whitelisted")));
                return;
            }
            server.setStatus(StatusData.WHITELISTED);
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.on.enabled")));
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new WhitelistBroadcastPacket(sender.getName(), server.getName(), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.on.staff-broadcast")));
        } else if (args[0].equalsIgnoreCase("off")) {
            Server server = OxygenBukkit.getInstance().getServerManager().serverFromName(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
            if (server == null) {
                sender.sendMessage(CC.translate("&cTheir was an error executing this command. (Error #2)"));
                return;
            }
            if (server.getStatus() != StatusData.WHITELISTED) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.not-whitelisted")));
                return;
            }
            server.setStatus(StatusData.ONLINE);
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.off.enabled")));
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new WhitelistBroadcastPacket(sender.getName(), server.getName(), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.on.staff-broadcast")));
        } else if (args[0].equalsIgnoreCase("toggle")) {
            Server server = OxygenBukkit.getInstance().getServerManager().serverFromName(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
            if (server == null) {
                sender.sendMessage(CC.translate("&cTheir was an error executing this command. (Error #3)"));
                return;
            }
            if (server.getStatus() == StatusData.WHITELISTED) {
                server.setStatus(StatusData.ONLINE);
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.on.enabled")));
                OxygenBukkit.getInstance().getRedisModule().sendPacket(new WhitelistBroadcastPacket(sender.getName(), server.getName(), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.on.staff-broadcast")));
            } else {
                server.setStatus(StatusData.WHITELISTED);
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.off.disabled")));
                OxygenBukkit.getInstance().getRedisModule().sendPacket(new WhitelistBroadcastPacket(sender.getName(), server.getName(), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.off.staff-broadcast")));
            }
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 2) {
                Server server = OxygenBukkit.getInstance().getServerManager().serverFromName(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
                if (server == null) {
                    sender.sendMessage(CC.translate("&cTheir was an error executing this command. (Error #4)"));
                    return;
                }
                String specifiedPlayer = args[1];
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(specifiedPlayer);
                if (profile == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_OFFLINE_PLAYER).replace("%player%", args[1]));
                    return;
                }
                if (server.getWhitelistedPlayers().contains(profile.getUniqueId())) {
                    OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.player-whitelisted");
                    return;
                }
                server.getWhitelistedPlayers().add(profile.getUniqueId());
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.added-player").replace("%player%", profile.getName())));
            } else {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.usage")));
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                Server server = OxygenBukkit.getInstance().getServerManager().serverFromName(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
                if (server == null) {
                    sender.sendMessage(CC.translate("&cTheir was an error executing this command. (Error #4)"));
                    return;
                }
                String specifiedPlayer = args[1];
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(specifiedPlayer);
                if (profile == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_OFFLINE_PLAYER).replace("%player%", args[1]));
                    return;
                }
                if (!server.getWhitelistedPlayers().contains(profile.getUniqueId())) {
                    OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.player-not-whitelisted");
                    return;
                }
                server.getWhitelistedPlayers().remove(profile.getUniqueId());
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.removed-player").replace("%player%", profile.getName())));
            } else {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.usage")));
            }
        } else {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("whitelist-command.usage")));
        }
    }
}
