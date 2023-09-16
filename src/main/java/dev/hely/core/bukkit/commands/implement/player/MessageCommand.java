package dev.hely.core.bukkit.commands.implement.player;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.player
//   Date: Wednesday, August 09, 2023 - 3:09 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.filter.Filter;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MessageCommand extends BaseCommand {
    public MessageCommand(){
        super("message", Collections.singletonList("msg"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.usage")));
            return;
        }

        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]) == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }
        Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (!Bukkit.getPlayer(targetProfile.getUniqueId()).isOnline()) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_OFFLINE_PLAYER).replace("%player%", args[0]));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);

        handleMessage(player, target, ChatUtil.joinArray(args, " ", 2));
    }

    private void handleMessage(Player sender, Player targetPlayer, String message) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(sender.getUniqueId());
        Profile target = OxygenBukkit.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId());
        Punishment mute = profile.getActiveMute();

        if (mute != null) {
            if (mute.isPermanent())
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.muted-permanent")));
            else {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.muted-temp").replace("%remaining%", mute.getRemaining())));
            }

            return;
        }

        if (!profile.getSettings().isReceivingNewConversations() && !sender.hasPermission("oxygen.staff")) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.messages-disabled")));
            return;
        }

        if (!target.getSettings().isReceivingNewConversations() && !sender.hasPermission("oxygen.staff")) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.not-receiving").replace("%player%", target.getName())));
            return;
        }

        if (!target.getIgnoredList().contains(sender.getName()) && !sender.hasPermission("oxygen.staff")) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.ignored").replace("%player%", target.getName())));
            return;
        }
        if (profile.getIgnoredList().contains(target.getName()) && !sender.hasPermission("oxygen.staff")) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.ignoring").replace("%player%", target.getName())));
            return;
        }
        if (target.getIgnoredList().contains(sender.getName()) && !sender.hasPermission("oxygen.staff")) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.ignored").replace("%player%", target.getName())));
            return;
        }

        Filter filter = OxygenBukkit.getInstance().getFilterManager().checkMessage(message);
        if (filter != null && !sender.hasPermission("oxygen.admin")) {
            filter.punish(message, profile, target);

            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.to-format").replace("%target%", target.getName()).replace("%message%", message)));
            return;
        }

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.to-format").replace("%target%", target.getName()).replace("%message%", message)));
        targetPlayer.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.from-format").replace("%target%", sender.getName()).replace("%message%", message)));

        if (target.getSettings().isPlayingMessageSounds()) {
            targetPlayer.playSound(targetPlayer.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 0.1f);
        }

        profile.setLastRepliedTo(target.getName());
        target.setLastRepliedTo(profile.getName());

        profile.setLastSentMessageMillis(System.currentTimeMillis());
        target.setLastReceivedMessageMillis(System.currentTimeMillis());

        for (Player current : Bukkit.getOnlinePlayers()) {
            if (!current.hasPermission("oxygen.socialspy")) {
                continue;
            }

            if (current.getUniqueId().equals(profile.getUniqueId()) || current.getUniqueId().equals(target.getUniqueId())) {
                continue;
            }

            Profile currentProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(current.getUniqueId());
            if (currentProfile.getStaffProfile().isSocialSpy()) {
                current.sendMessage(CC.translate(Locale.SOCIAL_SPY_FORMAT).replace("%sender%", sender.getName()).replace("%target%", targetPlayer.getName()).replace("%message%", message));
            }
        }

        if (!profile.getSettings().isReceivingNewConversations() && sender.hasPermission("oxygen.staff")) {
            targetPlayer.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.sent")));
        }
    }
}
