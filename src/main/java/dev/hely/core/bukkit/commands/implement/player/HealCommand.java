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

public class HealCommand extends BaseCommand {
    public HealCommand(){
        super("heal", Collections.singletonList("healplayer"),"oxygen.heal", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player senderPlayer = (Player) sender;
        if (args.length < 1) {
            Player player = (Player) sender;
            player.setFoodLevel(20);
            player.setSaturation(10.0F);
            player.setHealth(player.getMaxHealth());
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("heal-command.heal-self")));
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            if (senderPlayer.hasPermission("oxygen.heal.all")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player == null || !player.isOnline()) { // just making sure they didnt log off after check started
                        return;
                    }

                    player.setFoodLevel(20);
                    player.setSaturation(10.0F);
                    player.setHealth(player.getMaxHealth());
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("heal-command.heal-self")));
                }
            } else {
                senderPlayer.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            }
        } else {
            if (senderPlayer.hasPermission("oxygen.heal.others")) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("heal-command.not-online")));
                    return;
                }
                player.setFoodLevel(20);
                player.setSaturation(10.0F);
                player.setHealth(player.getMaxHealth());
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("heal-command.heal-other").replace("%player%", player.getName())));
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("heal-command.heal-self")));
            } else {
                senderPlayer.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            }
        }
    }
}
