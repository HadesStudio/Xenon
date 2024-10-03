package net.ascend.core.bukkit.commands.implement.player.message;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.filter.Filter;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.sound.SoundManager;
import org.bukkit.Bukkit;
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
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.usage")));
            return;
        }

        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (XenonBukkit.getInstance().getProfileManager().getProfile(args[0]) == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }
        Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (!Bukkit.getPlayer(targetProfile.getUniqueId()).isOnline()) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_OFFLINE_PLAYER).replace("%player%", args[0]));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        handleMessage(player, target, ChatUtil.joinArray(args, " ", 2));
    }

    private void handleMessage(Player sender, Player targetPlayer, String message) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(sender.getUniqueId());
        Profile target = XenonBukkit.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId());
        Punishment mute = profile.getActiveMute();

        if (mute != null) {
            if (mute.isPermanent())
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.muted-permanent")));
            else {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.muted-temp").replace("%remaining%", mute.getRemaining())));
            }

            return;
        }

        if (!profile.getSettings().isPrivateMessages() && !sender.hasPermission("xenon.staff")) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.messages-disabled")));
            return;
        }

        if (!target.getSettings().isPrivateMessages() && !sender.hasPermission("xenon.staff")) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.not-receiving").replace("%player%", target.getName())));
            return;
        }

        if (!target.getIgnoredList().contains(sender.getName()) && !sender.hasPermission("xenon.staff")) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.ignored").replace("%player%", target.getName())));
            return;
        }
        if (profile.getIgnoredList().contains(target.getName()) && !sender.hasPermission("xenon.staff")) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.ignoring").replace("%player%", target.getName())));
            return;
        }
        if (target.getIgnoredList().contains(sender.getName()) && !sender.hasPermission("xenon.staff")) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.ignored").replace("%player%", target.getName())));
            return;
        }

        Filter filter = XenonBukkit.getInstance().getFilterManager().checkMessage(message);
        if (filter != null && !sender.hasPermission("xenon.admin")) {
            filter.punish(message, profile, target);

            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.to-format").replace("%target%", target.getName()).replace("%message%", message)));
            return;
        }

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.to-format").replace("%target%", target.getName()).replace("%message%", message)));
        targetPlayer.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.from-format").replace("%target%", sender.getName()).replace("%message%", message)));

        if (target.getSettings().isSounds()) {
            SoundManager.INSTANCE.playSound(targetPlayer, "SUCCESSFUL_HIT");
        }

        profile.setLastRepliedTo(target.getName());
        target.setLastRepliedTo(profile.getName());

        profile.setLastSentMessageMillis(System.currentTimeMillis());
        target.setLastReceivedMessageMillis(System.currentTimeMillis());

        for (Player current : Bukkit.getOnlinePlayers()) {
            if (!current.hasPermission("xenon.socialspy")) {
                continue;
            }

            if (current.getUniqueId().equals(profile.getUniqueId()) || current.getUniqueId().equals(target.getUniqueId())) {
                continue;
            }

            Profile currentProfile = XenonBukkit.getInstance().getProfileManager().getProfile(current.getUniqueId());
            if (currentProfile.getStaffProfile().isSocialSpy()) {
                current.sendMessage(CC.translate(Locale.SOCIAL_SPY_FORMAT).replace("%sender%", sender.getName()).replace("%target%", targetPlayer.getName()).replace("%message%", message));
            }
        }

        if (!profile.getSettings().isPrivateMessages() && sender.hasPermission("xenon.staff")) {
            targetPlayer.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.sent")));
        }
    }
}
