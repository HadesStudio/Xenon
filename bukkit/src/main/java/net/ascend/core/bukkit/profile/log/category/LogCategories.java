package net.ascend.core.bukkit.profile.log.category;

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

import net.ascend.core.bukkit.XenonBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
public enum LogCategories {
    ALL(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.all-logs.display-name"),
            Material.valueOf(XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.all-logs.material")),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.all-logs.lore"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.all-logs.data"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.all-logs.slot")
    ),
    COMMAND_LOGS(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.command-logs.display-name"),
            Material.valueOf(XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.command-logs.material")),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.command-logs.lore"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.command-logs.data"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.command-logs.slot")
    ),
    CONNECTION_LOGS(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.connection-logs.display-name"),
            Material.valueOf(XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.connection-logs.material")),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.connection-logs.lore"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.connection-logs.data"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.connection-logs.slot")
    ),
    RANK_LOGS(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.rank-logs.display-name"),
            Material.valueOf(XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.rank-logs.material")),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.rank-logs.lore"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.rank-logs.data"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.rank-logs.slot")
    ),
    PUNISHMENT_LOGS(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.punishment-logs.display-name"),
            Material.valueOf(XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.punishment-logs.material")),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.punishment-logs.lore"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.punishment-logs.data"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.punishment-logs.slot")
    ),
    CURRENCY_LOGS(
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.currency-logs.display-name"),
            Material.valueOf(XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.currency-logs.material")),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getStringList("categories.currency-logs.lore"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.currency-logs.data"),
            XenonBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.currency-logs.slot")
    );

    @Getter String display_name;
    @Getter Material material;
    @Getter List<String> lore;
    @Getter int material_data;
    @Getter int slot;
}
