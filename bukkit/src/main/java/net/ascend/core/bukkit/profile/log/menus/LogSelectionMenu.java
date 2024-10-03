package net.ascend.core.bukkit.profile.log.menus;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.Log;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class LogSelectionMenu extends Menu {

    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("select-log-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (LogCategories value : LogCategories.values()) {
            buttons.put(value.getSlot(), new CategoryButton(profile, value));
        }
        return buttons;
    }


    @AllArgsConstructor
    public static class CategoryButton extends Button {

        private final Profile profile;
        private final LogCategories logCategories;

        public List<String> getLore() {
            int size = 0;
            for (Log log : XenonBukkit.getInstance().getLogManager().getLogMap().values()) {
                if (log.getUserUUID().equals(profile.getUniqueId())) {
                    if (log.getLogCategory() == logCategories || logCategories == LogCategories.ALL) {
                        size = size + 1;
                    }
                }
            }

            List<String> string = new ArrayList<>();
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
            new LogDisplayMenu(logCategories, profile).openMenu(player);
        }
    }
}
