package dev.hely.core.bukkit.coins.menu;

import dev.hely.core.bukkit.coins.menu.data.button.CustomButton;
import dev.hely.core.bukkit.coins.menu.data.category.CustomCategory;
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

/**
 * Created By LeandroSSJ
 * Created on 10/05/2022
 */
@AllArgsConstructor
public class CustomCategoryMenu extends Menu {

    {
        setClickUpdate(true);
    }

    private final CustomCategory customCategory;

    @Override
    public String getName() {
        return customCategory.getTitle();
    }

    @Override
    public String getTitle(Player player) {
        return customCategory.getTitle();
    }

    @Override
    public int getSize() {
        return customCategory.getRows() * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (customCategory.isFill_enabled()) {
            if (customCategory.getFill_style().equals("FILLED")) {
                for (int i = 0; i < getSize(); ++i) {
                    buttons.put(i, new SeparatorButton(customCategory));
                }
            } else if (customCategory.getFill_style().equals("CORNERS")) {
                for (String slot : customCategory.getFill_corners()) {
                    buttons.put(Integer.parseInt(slot) - 1, new SeparatorButton(customCategory));
                }
            }
        }

        if (customCategory.isBack_enabled()) {
            buttons.put(customCategory.getBack_slot() - 1, new BackButton(customCategory));
        }

        for (CustomButton customButton : customCategory.getCustomButtons()) {
            buttons.put(customButton.getSlot() - 1, new ItemButton(customButton));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class BackButton extends Button {

        private CustomCategory customCategory;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.valueOf(customCategory.getBack_material()))
                    .setDisplayName(customCategory.getBack_name())
                    .setData((short) customCategory.getBack_data())
                    .setLore(customCategory.getBack_lore());

            if (customCategory.isFill_glow()) {
                itemMaker.addFakeGlow();
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new CustomMenu().openMenu(player);
        }
    }

    @AllArgsConstructor
    private static class SeparatorButton extends Button {

        private final CustomCategory customCategory;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.valueOf("STAINED_GLASS_PANE")).setDisplayName("&8")
                    .setData((short) customCategory.getFill_data());

            if (customCategory.isFill_glow()) {
                itemMaker.addFakeGlow();
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }

    @AllArgsConstructor
    private static class ItemButton extends Button {

        private CustomButton customButton;

        @Override
        public ItemStack getItemStack(Player player) {
            List<String> lore = new ArrayList<>();
            for (String value : customButton.getLore()) {
                lore.add(value
                        .replace("%price%", "" + customButton.getPrice())
                        .replace("%d_arrow%", "\u00BB"));
            }

            return ItemMaker.copyOf(customButton.getButton()).setLore(lore).build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            customButton.execute(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
