package dev.hely.core.profile.log.category;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.log.category
//   Date: Monday, July 31, 2023 - 5:00 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
public enum LogCategories {
    ALL(
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.all-logs.display-name"),
            Material.valueOf(Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.all-logs.material")),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.all-logs.lore"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.all-logs.data"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.all-logs.slot")
    ),
    COMMAND_LOGS(
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.command-logs.display-name"),
            Material.valueOf(Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.command-logs.material")),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.command-logs.lore"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.command-logs.data"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.command-logs.slot")
    ),
    CONNECTION_LOGS(
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.connection-logs.display-name"),
            Material.valueOf(Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.connection-logs.material")),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.connection-logs.lore"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.connection-logs.data"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.connection-logs.slot")
    ),
    RANK_LOGS(
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.rank-logs.display-name"),
            Material.valueOf(Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.rank-logs.material")),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.rank-logs.lore"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.rank-logs.data"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.rank-logs.slot")
    ),
    PUNISHMENT_LOGS(
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.punishment-logs.display-name"),
            Material.valueOf(Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.punishment-logs.material")),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.punishment-logs.lore"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.punishment-logs.data"),
            Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.punishment-logs.slot")
    );

    @Getter String display_name;
    @Getter Material material;
    @Getter List<String> lore;
    @Getter int material_data;
    @Getter int slot;
}
