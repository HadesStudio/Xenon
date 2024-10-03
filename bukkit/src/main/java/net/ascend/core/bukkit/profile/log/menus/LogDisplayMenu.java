package net.ascend.core.bukkit.profile.log.menus;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.Log;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class LogDisplayMenu extends PaginatedMenu {

    private LogCategories categories;
    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("logs-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("logs-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(new LogSelectionMenu(profile)));
        buttons.put(1, Button.getPlaceholder());
        buttons.put(2, Button.getPlaceholder());
        buttons.put(3, Button.getPlaceholder());
        buttons.put(5, Button.getPlaceholder());
        buttons.put(6, Button.getPlaceholder());
        buttons.put(7, Button.getPlaceholder());
        buttons.put(8, Button.getPlaceholder());
        buttons.put(4, Button.getPlaceholder());

        buttons.put(36, new PageButton(-1, this));
        buttons.put(37, Button.getPlaceholder());
        buttons.put(38, Button.getPlaceholder());
        buttons.put(39, Button.getPlaceholder());
        buttons.put(40, Button.getPlaceholder());
        buttons.put(41, Button.getPlaceholder());
        buttons.put(42, Button.getPlaceholder());
        buttons.put(43, Button.getPlaceholder());
        buttons.put(44, new PageButton(1, this));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Map.Entry<Log, Long> sortedList : getSortedLogs().entrySet()) {
            Log logEntry = sortedList.getKey();
            if (logEntry.getUserUUID().equals(profile.getUniqueId())) {
                if (categories == LogCategories.ALL || logEntry.getLogCategory() == categories) {
                    buttons.put(buttons.size(), new LogButton(logEntry));
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
            ItemMaker builder = ItemMaker.of(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("logs-menu.button.material"));
            builder.setData((short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("logs-menu.button.data"));
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("logs-menu.button.display_name")
                    .replace("%log_display%", log.getLogType().getDisplay_name()));
            builder.setLore(getLore());

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }

    public Map<Log, Long> getSortedLogs() {
        Map<Long, Log> firstLogs = new HashMap<>(); // date, long

        for (Log log : XenonBukkit.getInstance().getLogManager().getLogMap().values()) {
            firstLogs.put(log.getDate(), log);
        }

        LinkedList<Map.Entry<Long, Log>> list = new LinkedList<>(firstLogs.entrySet());

        list.sort((o1, o2) -> (o2.getKey().compareTo(o1.getKey())));

        //Collections.reverse(list);

        LinkedHashMap<Log, Long> sortedHashMap = new LinkedHashMap<>();

        for (Map.Entry<Long, Log> entry : list) {
            sortedHashMap.put(entry.getValue(), entry.getKey());
        }

        return sortedHashMap;
    }

}
