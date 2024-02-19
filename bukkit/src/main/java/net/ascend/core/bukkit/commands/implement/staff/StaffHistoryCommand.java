package net.ascend.core.bukkit.commands.implement.staff;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.staff
//   Date: Wednesday, September 06, 2023 - 3:45 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.menu.StaffHistoryMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaffHistoryCommand  extends BaseCommand {
    public StaffHistoryCommand() {
        super("staffhistory", Arrays.asList("viewbans", "staffbans", "viewbandata", "viewstaffbans"), "xenon.command.staffhistory", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-history-command.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }
        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-history-command.scanning").replace("%player%", profile.getName())));
        Map<UUID, Punishment> executedPunishments = new HashMap<>();

        for (Document document : XenonBukkit.getInstance().getMongoModule().getPunishmentsCollection().find()) {
            Profile foundProfile = XenonBukkit.getInstance().getProfileManager().getProfile(UUID.fromString(document.getString("uniqueId")));
            if (foundProfile.getPunishments().isEmpty()) {
                continue;
            }

            for (Punishment punishment : foundProfile.getPunishments()) {
                if (punishment.getAddedBy().equals(profile.getName())) {
                    executedPunishments.put(foundProfile.getUniqueId(), punishment);
                }
            }
        }

        new StaffHistoryMenu(profile, executedPunishments).openMenu((Player) sender);
    }
}

