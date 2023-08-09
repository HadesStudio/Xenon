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
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.conversations.Conversation;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
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
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.usage")));
            return;
        }
        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (targetProfile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }
        if (!Bukkit.getPlayer(args[0]).isOnline()) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_OFFLINE_PLAYER).replace("%player%", args[0]));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);

        if (targetProfile.getConversations().canBeMessagedBy(player) && canSee(player, target)) {
            final Conversation conversation = profile.getConversations().getOrCreateConversation(target);
            targetProfile.getConversations().getOrCreateConversation(player);
            if (conversation.validate()) {
                if (!profile.getSettings().isReceivingNewConversations() || profile.getIgnoredList().contains(target.getName())) {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.messages-disabled")));
                    return;
                }
                conversation.sendMessage(player, target, ChatUtil.joinArray(args, " ", 2));
            }
            else {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.not-receiving").replace("%player%", target.getName())));
            }
        } else if (!canSee(player, target)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_OFFLINE_PLAYER).replace("%player%", target.getName()));
        } else {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("message-command.not-receiving").replace("%player%", target.getName())));
        }
    }

    public static boolean canSee(final Player sender, final Player target) {
        return target != null && sender.canSee(target);
    }
}
