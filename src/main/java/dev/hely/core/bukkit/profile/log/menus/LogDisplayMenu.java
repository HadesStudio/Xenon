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
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import dev.hely.core.bukkit.profile.log.category.LogCategories;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.InformationButton;
import dev.hely.lib.menu.button.implement.PageButton;
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
    private Map<Integer, Log> logs;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-viewer.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getLogsMenuConfig().getConfiguration().getString("log-viewer.title");
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, Button.getPlaceholder());
        buttons.put(1, Button.getPlaceholder());
        buttons.put(2, Button.getPlaceholder());
        buttons.put(3, Button.getPlaceholder());
        buttons.put(5, Button.getPlaceholder());
        buttons.put(6, Button.getPlaceholder());
        buttons.put(7, Button.getPlaceholder());
        buttons.put(8, Button.getPlaceholder());
        buttons.put(4, Button.getPlaceholder());

        buttons.put(9, Button.getPlaceholder());
        buttons.put(17, Button.getPlaceholder());
        buttons.put(18, Button.getPlaceholder());
        buttons.put(26, Button.getPlaceholder());
        buttons.put(27, Button.getPlaceholder());
        buttons.put(35, Button.getPlaceholder());

        buttons.put(36, Button.getPlaceholder());
        buttons.put(37, new PageButton(-1, this));
        buttons.put(38, Button.getPlaceholder());
        buttons.put(39, Button.getPlaceholder());
        buttons.put(40, Button.getPlaceholder());
        buttons.put(41, Button.getPlaceholder());
        buttons.put(42, Button.getPlaceholder());
        buttons.put(43, new PageButton(1, this));
        buttons.put(44, Button.getPlaceholder());

        return buttons;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Map.Entry<Integer, Log> entry : logs.entrySet()) {
            buttons.put(buttons.size(), new Button() {
                public List<String> getLore() {
                    List<String> defaultMessage = entry.getValue().getMessage();

                    return entry.getValue().formatMessage(defaultMessage);
                }

                @Override
                public ItemStack getItemStack(Player player) {
                    ItemMaker builder = ItemMaker.of(Material.PAPER);
                    builder.setData((short) 0);
                    builder.setDisplayName("&6Log - " + entry.getValue().getLogType().getDisplay_name());
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
