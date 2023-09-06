package dev.hely.core.bukkit.commands.implement.staff;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.player
//   Date: Saturday, August 26, 2023 - 4:14 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class HideStaffCommand extends BaseCommand {
    public static final ArrayList<UUID> hidden = new ArrayList<>();

    public HideStaffCommand() {
        super("hidestaff", Collections.singletonList("hs"), "oxygen.command.hidestaff", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (hidden.contains(player.getUniqueId())) {
            hidden.remove(player.getUniqueId());

            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("hide-staff.enabled")));

            for (UUID id : OxygenBukkit.getInstance().getStaffManager().getVanishedPlayers()) {
                if (Bukkit.getPlayer(id) == null) continue;
                player.showPlayer(Bukkit.getPlayer(id));
            }
            return;
        }

        hidden.add(player.getUniqueId());
        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("hide-staff.disabled")));

        for (UUID id : OxygenBukkit.getInstance().getStaffManager().getVanishedPlayers()) {
            if (Bukkit.getPlayer(id) == null) continue;
            player.hidePlayer(Bukkit.getPlayer(id));
        }
    }
}
