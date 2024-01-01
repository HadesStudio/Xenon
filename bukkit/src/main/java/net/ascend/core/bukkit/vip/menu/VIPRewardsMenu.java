package net.ascend.core.bukkit.vip.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.vip.menu
//   Date: Sunday, September 17, 2023 - 3:01 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.PageButton;
import net.ascend.lib.menu.implement.PaginatedMenu;
import net.ascend.lib.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class VIPRewardsMenu extends PaginatedMenu {
    Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("rewards-menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("rewards-menu.title");
    }

    @Override
    public int getSize() {
        return 45;
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());

        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(9, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());
        menuContent.put(18, Button.getPlaceholder());
        menuContent.put(26, Button.getPlaceholder());
        menuContent.put(27, Button.getPlaceholder());
        menuContent.put(35, Button.getPlaceholder());

        menuContent.put(36, Button.getPlaceholder());
        menuContent.put(37, new PageButton(-1, this));
        menuContent.put(38, Button.getPlaceholder());

        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, Button.getPlaceholder());
        menuContent.put(41, Button.getPlaceholder());

        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, new PageButton(1, this));
        menuContent.put(44, Button.getPlaceholder());

        return menuContent;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (String key : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getConfigurationSection("rewards").getKeys(false)) {
            List<String> lore = XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("rewards." + key + ".lore");
            String display_name = XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("rewards." + key + ".name");
            String material = XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("rewards." + key + ".material");
            int data = XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("rewards." + key + ".data");

            buttons.put(buttons.size(), new Reward_Button(profile, material, data, lore, display_name, key));
        }


        return buttons;
    }

    @AllArgsConstructor
    public static class Reward_Button extends Button {
        Profile profile;
        String material;
        int material_data;
        List<String> lore;
        String display_name;
        String key;

        public Material getMaterial() {
            if (material != null) {
                if (Material.getMaterial(material) != null) {
                    return Material.valueOf(material);
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of '" + key + " -material-reward' is null, using a default material."));
                    return Material.DIAMOND_BLOCK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of '" + key + " -material-reward' is null, using a default material."));
                return Material.DIAMOND_BLOCK;
            }
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData((short) material_data);
            builder.setDisplayName(display_name);
            builder.setLore(lore);

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }
}
