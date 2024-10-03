package net.ascend.core.bukkit.commands.implement.staff;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class HideStaffCommand extends BaseCommand {
    public static ArrayList<UUID> hidden = new ArrayList<>();

    public HideStaffCommand() {
        super("hidestaff", Collections.singletonList("hs"), "xenon.command.hidestaff", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (hidden.contains(player.getUniqueId())) {
            hidden.remove(player.getUniqueId());

            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("hidestaff-command.enabled")));

            /*for (UUID id : XenonBukkit.getInstance().getStaffManager().getVanishedPlayers()) {
                if (Bukkit.getPlayer(id) == null) continue;
                player.showPlayer(Bukkit.getPlayer(id));
            }*/
            return;
        }

        hidden.add(player.getUniqueId());
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("hidestaff-command.disabled")));

        /*for (UUID id : XenonBukkit.getInstance().getStaffManager().getVanishedPlayers()) {
            if (Bukkit.getPlayer(id) == null) continue;
            player.hidePlayer(Bukkit.getPlayer(id));
        }*/
    }
}
