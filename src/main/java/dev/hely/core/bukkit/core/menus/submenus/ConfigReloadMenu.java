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
import dev.hely.lib.configuration.Config;
import dev.hely.lib.util.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ConfigReloadMenu extends PaginatedMenu {
    {
        setClickUpdate(true);
    }
    Player player;

    @Override
    public String getName() {
        return player.getName() + " - Config Reload Menu";
    }

    @Override
    public String getTitle(Player player) {
        return player.getName() + " - Config Reload Menu";
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

        for (Config config : OxygenBukkit.getInstance().getConfigList()) {
            buttons.put(buttons.size(), new Config_Button(config));
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class Placeholder_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.STAINED_GLASS_PANE);
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
            new OxygenMenu(player).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class Config_Button extends Button {
        Config config;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.REDSTONE_BLOCK);
            builder.setData((short) 0);
            builder.setDisplayName("&6" + config.getFile().getName());
            builder.setLore(Arrays.asList(
                    "&7Below, is a few options you have",
                    "&7to save, or reload this configuration file.",
                    "&7",
                    "&6Left-Click &7to save this file.",
                    "&6Right-Click &7to reload this file.",
                    "&6Middle-Click &7to reload & save this file."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                try {
                    config.save();
                    player.sendMessage(CC.translate("&6[Oxygen] &cYou have successfully saved &6" + config.getFile().getName() + "&c!"));
                    player.playSound(player.getLocation(), org.bukkit.Sound.ORB_PICKUP, 5, 5);
                } catch (IOException | InvalidConfigurationException e) {
                    ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

                    bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                    bukkitCommandSender.sendMessage(CC.translate("&6Oxygen Core - Save Configuration Error"));
                    bukkitCommandSender.sendMessage(CC.translate("&6File: &c" + config.getFile().getName()));
                    bukkitCommandSender.sendMessage(CC.translate("&c"));
                    bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
                    bukkitCommandSender.sendMessage(CC.translate("&cError:"));
                    e.printStackTrace();
                    bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                }
            } else if (clickType.isRightClick()) {
                try {
                    config.reload();
                    player.sendMessage(CC.translate("&6[Oxygen] &cYou have successfully reloaded &6" + config.getFile().getName() + "&c!"));
                    player.playSound(player.getLocation(), org.bukkit.Sound.ORB_PICKUP, 5, 5);
                } catch (IOException | InvalidConfigurationException e) {
                    ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

                    bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                    bukkitCommandSender.sendMessage(CC.translate("&6Oxygen Core - Reload Configuration Error"));
                    bukkitCommandSender.sendMessage(CC.translate("&6File: &c" + config.getFile().getName()));
                    bukkitCommandSender.sendMessage(CC.translate("&c"));
                    bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
                    bukkitCommandSender.sendMessage(CC.translate("&cError:"));
                    e.printStackTrace();
                    bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                }
            } else {
                try {
                    config.save();
                    config.reload();
                    player.sendMessage(CC.translate("&6[Oxygen] &cYou have successfully saved & reloaded &6" + config.getFile().getName() + "&c!"));
                    player.playSound(player.getLocation(), org.bukkit.Sound.ORB_PICKUP, 5, 5);
                } catch (IOException | InvalidConfigurationException e) {
                    ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

                    bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                    bukkitCommandSender.sendMessage(CC.translate("&6Oxygen Core - Save Configuration Error"));
                    bukkitCommandSender.sendMessage(CC.translate("&6File: &c" + config.getFile().getName()));
                    bukkitCommandSender.sendMessage(CC.translate("&c"));
                    bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
                    bukkitCommandSender.sendMessage(CC.translate("&cError:"));
                    e.printStackTrace();
                    bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                }
            }
        }
    }
}
