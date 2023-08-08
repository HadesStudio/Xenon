package dev.hely.core.bukkit.coins.menu;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.menu.data.button.CustomButton;
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
public class CustomMenu extends Menu {

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getInt("menu.rows") * 9;
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getBoolean("menu.fill.enabled")) {
            if (OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("menu.fill.style").equals("FILLED")) {
                for (int i = 0; i < getSize(); ++i) {
                    buttons.put(i, new SeparatorButton());
                }
            } else if (OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("menu.fill.style").equals("CORNERS")) {
                for (String slot : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("menu.fill.slots")) {
                    buttons.put(Integer.parseInt(slot) - 1, new SeparatorButton());
                }
            }
        }


        for (CustomButton customButton : OxygenBukkit.getInstance().getStoreManager().getCustomButtonStorage()) {
            buttons.put(customButton.getSlot() - 1, new MenuButton(customButton));
        }

        return buttons;
    }


    private static class SeparatorButton extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.valueOf("STAINED_GLASS_PANE")).setDisplayName("&8")
                    .setData((short) OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getInt("menu.fill.data"));

            if (OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getBoolean("menu.fill.glow")) {
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
            for (String value : customButton.getLore()) {
                lore.add(value.replace("%price%", "" + customButton.getPrice())
                        .replace("%d_arrow%", "\u00BB"));
            }

            return ItemMaker.copyOf(customButton.getButton()).setLore(lore).build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            customButton.execute(player);
        }
    }
}
