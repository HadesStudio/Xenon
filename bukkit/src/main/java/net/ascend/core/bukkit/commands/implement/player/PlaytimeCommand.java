package net.ascend.core.bukkit.commands.implement.player;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.player
//   Date: Thursday, August 24, 2023 - 6:02 PM
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
import net.ascend.core.bukkit.lib.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PlaytimeCommand extends BaseCommand {

    public PlaytimeCommand(){
        super("playtime", Collections.singletonList("pt"), true);
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.view.global").replace("%time%", TimeUtil.getMoreDetailedTime(profile.getTotalPlayTime()))));
            return;
        } else {
            Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
            if (targetProfile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                return;
            }

            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.view.global-player").replace("%time%", TimeUtil.getMoreDetailedTime(targetProfile.getTotalPlayTime())).replace("%player%", targetProfile.getName())));
        }
        if (args[0].equalsIgnoreCase("top")) {
            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("playtime-command.top.top-format")) {
                player.sendMessage(CC.translate(message));
            }

            AtomicInteger count = new AtomicInteger(1);

            Map<Profile, Long> playTimes = new HashMap<>();
            playTimes.clear();
            for (Document document : XenonBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
                UUID uuid = UUID.fromString(document.getString("uniqueId"));
                Profile playerProfile = XenonBukkit.getInstance().getProfileManager().getProfile(uuid);
                if (playerProfile == null) {
                    break;
                }
                playTimes.put(playerProfile, document.getLong("playTime"));
            }

            playTimes.entrySet().stream().sorted(Map.Entry.<Profile, Long>comparingByValue().reversed()).forEach(msg -> sender.sendMessage(CC.translate( XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.top.format").replace("%number%", String.valueOf(count.getAndIncrement())).replace("%player%", msg.getKey().getName()).replace("%time%", TimeUtil.getMoreDetailedTime(msg.getValue())))));

            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("playtime-command.top.bottom-format")) {
                player.sendMessage(CC.translate(message));
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!player.hasPermission("xenon.playtime.remove")) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
                return;
            }

            if(args.length < 3) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.remove.usage")));
                return;
            }

            Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
            if (targetProfile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
                return;
            }
            long duration = TimeUtil.parseTime(args[2]);
            if(duration == -1) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.remove.invalid-time")));
                return;
            }

            if (targetProfile.getPlayTime() < duration) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.remove.cannot-remove")));
                return;
            }

            targetProfile.setPlayTime(targetProfile.getPlayTime() - duration);
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.remove.removed").replace("%player%", targetProfile.getName()).replace("%time%", TimeUtil.getMoreDetailedTime(duration))));

            Player targetPlayer = Bukkit.getPlayer(targetProfile.getUniqueId());
            if (targetPlayer != null && targetPlayer.isOnline()) {
                targetPlayer.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.remove.alert").replace("%player%", sender.getName()).replace("%time%", TimeUtil.getMoreDetailedTime(duration))));
            }
        } else if (args[0].equalsIgnoreCase("add")) {
            if (!player.hasPermission("xenon.playtime.add")) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
                return;
            }

            if(args.length < 3) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.add.usage")));
                return;
            }

            Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
            if (targetProfile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
                return;
            }
            long duration = TimeUtil.parseTime(args[2]);
            if(duration == -1) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.add.invalid-time")));
                return;
            }

            if (targetProfile.getPlayTime() < duration) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.add.cannot-remove")));
                return;
            }

            targetProfile.setPlayTime(targetProfile.getPlayTime() - duration);
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.add.removed").replace("%player%", targetProfile.getName()).replace("%time%", TimeUtil.getMoreDetailedTime(duration))));

            Player targetPlayer = Bukkit.getPlayer(targetProfile.getUniqueId());
            if (targetPlayer != null && targetPlayer.isOnline()) {
                targetPlayer.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("playtime-command.add.alert").replace("%player%", sender.getName()).replace("%time%", TimeUtil.getMoreDetailedTime(duration))));
            }
        }
    }
}