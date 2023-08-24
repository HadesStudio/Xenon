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
import dev.hely.core.bukkit.core.menus.OxygenMenu;
import dev.hely.core.bukkit.profile.log.Log;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import dev.hely.core.bukkit.profile.log.category.LogCategories;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.InformationButton;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class LogDisplayMenu extends PaginatedMenu {
    private LogCategories categories;
    private OfflinePlayer offlinePlayer;

    static int size = 0;

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
        buttons.put(0, new Back_Button(offlinePlayer));
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

        for (Map.Entry<Log, Long> sortedList : getSortedLogs().entrySet()) {
            Log logEntry = sortedList.getKey();
            if (logEntry.getUserUUID().equals(offlinePlayer.getUniqueId())) {
                if (categories == LogCategories.ALL || logEntry.getLogCategory() == categories) {
                    buttons.put(buttons.size(), new LogButton(logEntry));
                    size = size + 1;
                }
            }
        }

        return buttons;
    }


    @AllArgsConstructor
    public static class LogButton extends Button {
        final Log log;

        public List<String> getLore() {
            List<String> defaultMessage = log.getMessage();

            return log.formatMessage(defaultMessage);
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.PAPER);
            builder.setData((short) 0);
            builder.setDisplayName("&6Log - " + log.getLogType().getDisplay_name());
            builder.setLore(getLore());

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }

    @AllArgsConstructor
    public static class Back_Button extends Button {
        private OfflinePlayer offlinePlayer;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.BED);
            builder.setData((short) 0);
            builder.setDisplayName("&cBack");
            builder.setLore(Collections.singletonList(
                    "&7Click here to return to the main menu."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new LogSelectionMenu(offlinePlayer).openMenu(player);
        }
    }

    public Map<Log, Long> getSortedLogs() {
        final Map<Long, Log> firstLogs = new HashMap<>(); // date, long

        for (Log log : OxygenBukkit.getInstance().getLogManager().getLogMap().values()) {
            firstLogs.put(log.getDate(), log);
        }

        LinkedList<Map.Entry<Long, Log>> list = new LinkedList<>(firstLogs.entrySet());

        list.sort((o1, o2) -> (o2.getKey().compareTo(o1.getKey())));

        Collections.reverse(list);

        LinkedHashMap<Log, Long> sortedHashMap = new LinkedHashMap<>();

        for (Map.Entry<Long, Log> entry : list) {
            sortedHashMap.put(entry.getValue(), entry.getKey());
        }

        return sortedHashMap;
    }

}
