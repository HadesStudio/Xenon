package net.ascend.lib.menu.button.implement;

import com.google.common.collect.Lists;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.implement.FilterablePaginatedMenu;
import net.ascend.lib.menu.implement.PageFilter;
import net.ascend.lib.CC;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class PageFilterButton<T> extends Button {
    private FilterablePaginatedMenu<T> menu;

    @Override
    public ItemStack getItemStack(Player player) {
        if (menu.getFilters() == null || menu.getFilters().isEmpty()) {
            return new ItemStack(Material.AIR);
        }

        List<String> lore = Lists.newArrayList();
        lore.add("");

        for (PageFilter<?> filter : menu.getFilters()) {
            String color;
            String decoration = "";
            String icon;

            if (filter.isEnabled()) {
                color = ChatColor.GOLD.toString();
                icon = StringEscapeUtils.unescapeJava("\u2713");
            } else {
                color = ChatColor.RED.toString();
                icon = StringEscapeUtils.unescapeJava("\u2717");
            }

            if (menu.getFilters().get(menu.getScrollIndex()).equals(filter)) {
                decoration = ChatColor.YELLOW + StringEscapeUtils.unescapeJava("» ") + " ";
            }

            lore.add(decoration + color + icon + " " + filter.getName());
        }

        lore.add("");
        lore.add("&6Left-Click &7to change filter option.");
        lore.add("&6Right-Click &7to toggle your filter.");
        lore.add("");

        return ItemMaker.of(Material.HOPPER)
                .setDisplayName("&6Page Filters")
                .setLore(lore)
                .build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (menu.getFilters() == null || menu.getFilters().isEmpty()) {
            player.sendMessage(CC.translate("&cSorry, this menu does not have any filters setup."));
        } else {
            if (clickType == ClickType.LEFT) {
                if (menu.getScrollIndex() == menu.getFilters().size() - 1) {
                    menu.setScrollIndex(0);
                } else {
                    menu.setScrollIndex(menu.getScrollIndex() + 1);
                }
            } else if (clickType == ClickType.RIGHT) {
                PageFilter<T> filter = menu.getFilters().get(menu.getScrollIndex());
                filter.setEnabled(!filter.isEnabled());
            }
        }
    }

    @Override
    public boolean toUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

