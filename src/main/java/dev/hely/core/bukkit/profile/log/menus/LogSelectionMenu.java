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
import dev.hely.core.bukkit.profile.log.Log;
import dev.hely.core.bukkit.profile.log.category.LogCategories;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class LogSelectionMenu extends Menu {
    {
        setPlaceholder(true);
    }
    private OfflinePlayer offlinePlayer;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.title");
    }

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getInt("categories.size");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("categories.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (LogCategories value : LogCategories.values()) {
            buttons.put(value.getSlot(), new CategoryButton(offlinePlayer, value));
        }
        return buttons;
    }


    @AllArgsConstructor
    public static class CategoryButton extends Button {
        final OfflinePlayer offlinePlayer;
        final LogCategories logCategories;

        public List<String> getLore() {
            final List<String> string = new ArrayList<>();
            int size = 0;
            for (Log log : OxygenBukkit.getInstance().getLogManager().getLogMap().values()) {
                if (log.getUserUUID().equals(offlinePlayer.getUniqueId())) {
                    if (log.getLogCategory() == logCategories || logCategories == LogCategories.ALL) {
                        size = size + 1;
                    }
                }
            }

            for (String line : logCategories.getLore()) {
                string.add(line
                        .replace("%log_size%", String.valueOf(size))
                );
            }

            return string;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(logCategories.getMaterial());
            builder.setData((short) logCategories.getMaterial_data());
            builder.setDisplayName(logCategories.getDisplay_name());
            builder.setLore(getLore());

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new LogDisplayMenu(logCategories, offlinePlayer).openMenu(player);
        }
    }
}
