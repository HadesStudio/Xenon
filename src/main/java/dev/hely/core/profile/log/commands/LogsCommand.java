package dev.hely.core.profile.log.commands;

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

import com.mongodb.client.MongoCollection;
import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.log.category.LogCategories;
import dev.hely.core.profile.log.menus.LogSelectionMenu;
import dev.hely.core.profile.log.types.LogActionTypes;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LogsCommand extends BaseCommand {

    public LogsCommand(){
        super("logs", "oxygen.staff.logs");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("messages.usage")));
            return;
        }

        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        OfflinePlayer player = Bukkit.getPlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, player, args[0])) return;

        Map<LogActionTypes, Map<String, Object>> info = new HashMap<>();
        MongoCollection<Document> collection = Oxygen.getInstance().getMongoModule().getPlayerLogsCollection();
        for (Document document : collection.find(new Document("playerUUID", player.getUniqueId().toString()))) {
            info.put(LogActionTypes.valueOf(document.getString("logType")), (Map<String, Object>) document.get("params"));
            System.out.print("------------------------------------------------");
            System.out.print("Log Type - " + LogActionTypes.valueOf(document.getString("logType")));
            System.out.print(document.get("Parameters - " + document.get("params")));
            System.out.print("Player Name - " + player.getName());
            System.out.print("UUID - " + player.getUniqueId());
            System.out.print("------------------------------------------------");
        }



        new LogSelectionMenu(player, info).openMenu((Player) sender);
    }
}
