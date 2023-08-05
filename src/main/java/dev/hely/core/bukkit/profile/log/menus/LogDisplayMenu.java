package dev.hely.core.bukkit.profile.log.menus;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import dev.hely.core.bukkit.profile.log.category.LogCategories;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
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
public class LogDisplayMenu extends PaginatedMenu {
    private OfflinePlayer player;
    private LogCategories category;
    private Map<LogActionTypes, Map<String, Object>> logs;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-viewer.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-viewer.title");
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Map.Entry<LogActionTypes, Map<String, Object>> entry : logs.entrySet()) {
            buttons.put(buttons.size(), new Button() {
                public List<String> getLore() {
                    final List<String> string = new ArrayList<>();

                    for (Map.Entry<String, Object> entry2 : entry.getValue().entrySet()) {
                        string.add(entry2.getKey() + " <-- Result: " + entry2.getValue());
                    }

                    return string;
                }

                @Override
                public ItemStack getItemStack(Player player) {
                    ItemMaker builder = ItemMaker.of(Material.PAPER);
                    builder.setData((short) 0);
                    builder.setDisplayName("&6" + entry.getKey().getDisplay_name());
                    builder.setLore(getLore());

                    return builder.build();
                }

                @Override
                public void onClick(Player player, int slot, ClickType clickType) {
                }
            });

        }

        return buttons;
    }

}
