package dev.hely.core.bukkit.commands.implement.player;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.player
//   Date: Monday, September 04, 2023 - 7:32 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class FeedCommand extends BaseCommand {
    public FeedCommand(){
        super("feed", Collections.singletonList("feedplayer"),"oxygen.feed", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player senderPlayer = (Player) sender;
        if (args.length < 1) {
            Player player = (Player) sender;
            player.setFoodLevel(20);
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.feed-self")));
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            if (senderPlayer.hasPermission("oxygen.feed.all")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player == null || !player.isOnline()) { // just making sure they didnt log off after check started
                        return;
                    }

                    player.setFoodLevel(20);
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.feed-self")));
                }
            } else {
                senderPlayer.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            }
        } else {
            if (senderPlayer.hasPermission("oxygen.feed.others")) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.not-online")));
                    return;
                }
                player.setFoodLevel(20);
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.feed-other").replace("%player%", player.getName())));
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.feed-self")));
            } else {
                senderPlayer.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            }
        }
    }
}
