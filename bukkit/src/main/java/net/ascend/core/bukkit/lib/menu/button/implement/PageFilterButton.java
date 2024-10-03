package net.ascend.core.bukkit.lib.menu.button.implement;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.FilterablePaginatedMenu;
import net.ascend.core.bukkit.lib.menu.implement.PageFilter;
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
                color = ChatColor.GREEN.toString();
                icon = StringEscapeUtils.unescapeJava("\u2713");
            } else {
                color = ChatColor.GRAY.toString();
                icon = StringEscapeUtils.unescapeJava("\u2717");
            }

            if (menu.getFilters().get(menu.getScrollIndex()).equals(filter)) {
                decoration = ChatColor.GRAY + StringEscapeUtils.unescapeJava("» ") + " ";
            }

            lore.add(decoration + color + icon + " " + filter.getName());
        }

        lore.add("");
        lore.add("&7Left-Click to change filter option.");
        lore.add("&7Right-Click to toggle your filter.");

        return ItemMaker.of("HOPPER")
                .setDisplayName("&aPage Filters")
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

