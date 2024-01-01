package net.ascend.core.bukkit.vip;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.vip
//   Date: Saturday, September 16, 2023 - 3:09 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VIPRewardProvider {
    public List<String> getCommands(Player player) {
        List<String> commands = new ArrayList<>();

        for (String key : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getConfigurationSection("rewards").getKeys(false)) {
            for (String command : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("rewards." + key + ".commands")) {
                commands.add(command.replace("%player%", player.getName()));
            }
        }
        return commands;
    }
}
