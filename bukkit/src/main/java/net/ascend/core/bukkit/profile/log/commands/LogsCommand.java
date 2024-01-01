package net.ascend.core.bukkit.profile.log.commands;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.log.menus.LogSelectionMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogsCommand extends BaseCommand {
    int size = 0;

    public LogsCommand(){
        super("logs", "xenon.staff.logs");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("messages.usage")));
            return;
        }

        if (XenonBukkit.getInstance().getProfileManager().getProfile(args[0]) == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        OfflinePlayer player = Bukkit.getPlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, player, args[0])) return;

        new LogSelectionMenu(player).openMenu((Player) sender);
    }
}
