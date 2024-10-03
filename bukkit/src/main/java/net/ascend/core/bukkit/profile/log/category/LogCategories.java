package net.ascend.core.bukkit.profile.log.category;

import net.ascend.core.bukkit.XenonBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
public enum LogCategories {
    ALL(
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.all-logs.display_name"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.all-logs.material"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("select-log-menu.buttons.all-logs.lore"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.all-logs.data"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.all-logs.slot")
    ),

    COMMAND_LOGS(
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.command-logs.display_name"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.command-logs.material"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("select-log-menu.buttons.command-logs.lore"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.command-logs.data"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.command-logs.slot")
    ),
    CONNECTION_LOGS(
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.connection-logs.display_name"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.connection-logs.material"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("select-log-menu.buttons.connection-logs.lore"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.connection-logs.data"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.connection-logs.slot")
    ),
    RANK_LOGS(
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.rank-logs.display_name"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.rank-logs.material"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("select-log-menu.buttons.rank-logs.lore"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.rank-logs.data"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.rank-logs.slot")
    ),
    PUNISHMENT_LOGS(
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.punishment-logs.display_name"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.punishment-logs.material"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("select-log-menu.buttons.punishment-logs.lore"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.punishment-logs.data"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.punishment-logs.slot")
    ),
    CURRENCY_LOGS(
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.currency-logs.display_name"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.buttons.currency-logs.material"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("select-log-menu.buttons.currency-logs.lore"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.currency-logs.data"),
            XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("select-log-menu.buttons.currency-logs.slot")
    );

    @Getter private final String display_name;
    @Getter private final String material;
    @Getter private final List<String> lore;
    @Getter private final int material_data;
    @Getter private final int slot;
}
