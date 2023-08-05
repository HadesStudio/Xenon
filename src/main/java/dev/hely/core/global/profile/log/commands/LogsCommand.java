package dev.hely.core.global.profile.log.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.log.commands
//   Date: Monday, July 31, 2023 - 7:03 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.global.profile.log.types.LogActionTypes;
import dev.hely.core.global.profile.log.menus.LogSelectionMenu;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogsCommand extends BaseCommand {

    public LogsCommand(){
        super("logs", "oxygen.staff.logs");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("messages.usage")));
            return;
        }

        if (OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]) == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        OfflinePlayer player = Bukkit.getPlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, player, args[0])) return;

        Map<LogActionTypes, Map<String, Object>> info = new HashMap<>();
        OxygenBukkit.getInstance().getMongoModule().getPlayerLogsCollection().find().into(new ArrayList<>()).forEach(document -> {
            System.out.print("------------------------------------------------");
            System.out.print("Log Command Debug:");
            System.out.print("");
            System.out.print("Log Type - " + LogActionTypes.valueOf(document.getString("logType")));
            System.out.print(document.get("Parameters - " + document.get("params")));
            System.out.print("Player Name - " + player.getName());
            System.out.print("UUID - " + player.getUniqueId());
            System.out.print("------------------------------------------------");
            info.put(LogActionTypes.valueOf(document.getString("logType")), (Map<String, Object>) document.get("params"));
        });



        new LogSelectionMenu(player, info).openMenu((Player) sender);
    }
}
