package dev.hely.core.bukkit.tag.menu;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.tag.menu.data.button.CustomButton;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomMenu extends Menu {

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getInt("category-menu.rows") * 9;
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("category-menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("category-menu.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getBoolean("category-menu.fill.enabled")) {
            if (OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("category-menu.fill.style").equals("FILLED")) {
                for (int i = 0; i < getSize(); ++i) {
                    buttons.put(i, new SeparatorButton());
                }
            } else if (OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("category-menu.fill.style").equals("CORNERS")) {
                for (String slot : OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getStringList("category-menu.fill.slots")) {
                    buttons.put(Integer.parseInt(slot) - 1, new SeparatorButton());
                }
            }
        }


        for (CustomButton customButton : OxygenBukkit.getInstance().getTagManager().getCustomButtonStorage()) {
            buttons.put(customButton.getSlot() - 1, new MenuButton(customButton));
        }

        return buttons;
    }


    private static class SeparatorButton extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.valueOf("STAINED_GLASS_PANE")).setDisplayName("&8")
                    .setData((short) OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getInt("category-menu.fill.data"));

            if (OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getBoolean("category-menu.fill.glow")) {
                itemMaker.addFakeGlow();
            }
            return itemMaker.build();
        }
    }

    @AllArgsConstructor
    private static class MenuButton extends Button {
        private CustomButton customButton;

        @Override
        public ItemStack getItemStack(Player player) {
            List<String> lore = new ArrayList<>();
            for (String value : customButton.getOwnsLore()) {
                lore.add(value.replace("%d_arrow%", "\u00BB"));
            }

            return ItemMaker.copyOf(customButton.getButton()).setLore(lore).build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            customButton.execute(player);
        }
    }
}
