package dev.hely.core.profile.log.menus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.log.menus
//   Date: Monday, July 31, 2023 - 6:47 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.profile.log.category.LogCategories;
import dev.hely.core.profile.log.types.LogActionTypes;
import dev.hely.core.staff.menu.RequestsMainMenu;
import dev.hely.core.staff.menu.submenus.ActiveRequestsMenu;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class LogSelectionMenu extends Menu {
    {
        setPlaceholder(true);
    }
    private OfflinePlayer player;
    private Map<LogActionTypes, Map<String, Object>> logs;

    @Override
    public String getName() {
        return Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.title");
    }

    @Override
    public int getSize() {
        return Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.size");
    }

    @Override
    public String getTitle(Player player) {
        return Oxygen.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (LogCategories value : LogCategories.values()) {
            buttons.put(value.getSlot(), new Button() {
                public List<String> getLore() {
                    final List<String> string = new ArrayList<>();
                    int logCount = logs.values().size();

                    for (String line : value.getLore()) {
                        string.add(line
                                .replace("%request_size%", String.valueOf(Oxygen.getInstance().getRequestHandler().generateClosedRequestInteger()))
                                .replace("%log_size%", String.valueOf(logCount))
                        );
                    }

                    return string;
                }

                @Override
                public ItemStack getItemStack(Player player) {
                    ItemMaker builder = ItemMaker.of(value.getMaterial());
                    builder.setData((short) value.getMaterial_data());
                    builder.setDisplayName(value.getDisplay_name());
                    builder.setLore(getLore());

                    return builder.build();
                }

                @Override
                public void onClick(Player player, int slot, ClickType clickType) {
                    Map<LogActionTypes, Map<String, Object>> params = new HashMap<>();

                    logs.entrySet().stream().filter(it -> value == LogCategories.ALL || it.getKey().getCategory() == value).forEach(it -> params.put(it.getKey(), it.getValue()));
                    new LogDisplayMenu(player, value, params).openMenu(player);

                    for (Map.Entry<LogActionTypes, Map<String, Object>> entry : logs.entrySet()) {
                        System.out.print("----------------------------------");
                        System.out.print("Type - " + entry.getKey().getDisplay_name());
                        System.out.print("Category - " + entry.getKey().getCategory());
                        for (Map.Entry<String, Object> entry2 : entry.getValue().entrySet()) {
                            System.out.print(entry2.getKey() + " <-- Result: " + entry2.getValue());
                        }
                        System.out.print("----------------------------------");
                    }
                }
            });

        }

        return buttons;
    }
}
