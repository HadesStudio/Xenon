package dev.hely.core.bukkit.core.menus.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.core.menus.submenus
//   Date: Saturday, August 05, 2023 - 2:45 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.core.menus.OxygenMenu;
import dev.hely.core.bukkit.core.menus.buttons.InfoButton;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.utils.material.MaterialUtil;
import dev.hely.core.bukkit.utils.sound.SoundUtil;
import dev.hely.lib.util.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ModulesMenu extends PaginatedMenu {
    {
        setClickUpdate(true);
    }
    Player player;

    @Override
    public String getName() {
        return player.getName() + " - Modules Menu";
    }

    @Override
    public String getTitle(Player player) {
        return player.getName() + " - Modules Menu";
    }

    @Override
    public int getSize() {
        return 45;
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new Back_Button());
        menuContent.put(1, new Placeholder_Button());
        menuContent.put(4, new InfoButton());
        menuContent.put(7, new Placeholder_Button());
        menuContent.put(8, new Placeholder_Button());
        menuContent.put(9, new Placeholder_Button());
        menuContent.put(17, new Placeholder_Button());
        menuContent.put(18, new PageButton(-1, this));
        menuContent.put(26, new PageButton(1, this));
        menuContent.put(27, new Placeholder_Button());
        menuContent.put(35, new Placeholder_Button());
        menuContent.put(36, new Placeholder_Button());
        menuContent.put(37, new Placeholder_Button());
        menuContent.put(43, new Placeholder_Button());
        menuContent.put(44, new Placeholder_Button());

        return menuContent;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (ModuleLoader module : OxygenBukkit.getInstance().getModules()) {
            buttons.put(buttons.size(), new Module_Button(module));
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class Placeholder_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(MaterialUtil.STAINED_GLASS_PANE.getMaterial());
            builder.setData((short) 1);
            builder.setDisplayName("&7");
            builder.addFakeGlow();
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }

    @AllArgsConstructor
    public static class Back_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(MaterialUtil.BED.getMaterial());
            builder.setData((short) 0);
            builder.setDisplayName("&cBack");
            builder.setLore(Collections.singletonList(
                    "&7Click here to return to the main menu."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new OxygenMenu(player).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class Module_Button extends Button {
        ModuleLoader module;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.PAPER);
            builder.setData((short) 0);
            builder.setDisplayName("&6" + module.getName());
            builder.setLore(Arrays.asList(
                    "&7Below, is a few options you have",
                    "&7to modify this module.",
                    "&7",
                    "&6Left-Click &7to reload this module."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                module.reload();
                player.sendMessage(CC.translate("&6[Oxygen] &cYou have successfully reloaded &6" + module.getName() + "&c!"));
                SoundUtil.ORB_PICKUP.play(player, 5, 5);
                Bukkit.getConsoleSender().sendMessage(CC.translate("&6[Oxygen] &a" + player.getName() + " &chas just reloaded the &4" + module.getName() + " Module&c."));
            }
        }
    }
}
