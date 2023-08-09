package dev.hely.core.bukkit.commands.implement.player;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.player
//   Date: Wednesday, August 09, 2023 - 2:06 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.conversations.Conversation;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ReplyCommand extends BaseCommand {
    public ReplyCommand(){
        super("reply", Collections.singletonList("r"), true);
    }
    private static long VANISH_REPLY_TIMEOUT; // If they are in vanish, remove it after a certain duration.

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()) == null) {
            player.sendMessage(CC.translate("&cYour profile is null somehow? Please create a ticket in Hely Development discord."));
            return;
        }
        final Profile playerProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final Conversation conversation = playerProfile.getConversations().getLastRepliedConversation();
        if (args.length < 1) {
            if (conversation != null) {
                final Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(conversation.getPartner(player.getUniqueId()));
                targetProfile.getConversations().getOrCreateConversation(player);

                if (conversation.validate()) {
                    final long currentTimeMillis = System.currentTimeMillis();
                    if (!canSee(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId()))) && currentTimeMillis - playerProfile.getLastReceivedMessageMillis() > TimeUnit.SECONDS.toMillis(45L)) {
                        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reply-command.no-last-message")));
                        return;
                    }
                    if (!targetProfile.getConversations().canBeMessagedBy(player) && canSee(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())))) {
                        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reply-command.not-receiving").replace("%player%", targetProfile.getName())));
                        return;
                    }
                    if (!playerProfile.getSettings().isReceivingNewConversations() || playerProfile.getIgnoredList().contains(Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())).getName())) {
                        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reply-command.messages-disabled")));
                        return;
                    }
                    conversation.sendMessage(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())), ChatUtil.joinArray(args, " ", 1));
                }
            }
        } else {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reply-command.usage")));
        }
    }
    public static boolean canSee(final Player sender, final Player target) {
        return target != null && sender.canSee(target);
    }

    static {
        ReplyCommand.VANISH_REPLY_TIMEOUT = TimeUnit.SECONDS.toMillis(45L);
    }
}
