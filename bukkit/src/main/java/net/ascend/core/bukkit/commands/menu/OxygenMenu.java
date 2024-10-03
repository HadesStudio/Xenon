package net.ascend.core.bukkit.commands.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.core.menus
//   Date: Saturday, August 05, 2023 - 2:12 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.commands.menu.submenus.CommandsMenu;
import net.ascend.core.bukkit.commands.menu.submenus.ConfigReloadMenu;
import net.ascend.core.bukkit.commands.menu.submenus.ModulesMenu;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.server.menu.ServerListMenu;
import net.ascend.core.bukkit.lib.configuration.Config;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class OxygenMenu extends Menu {

    {
        setClickUpdate(true);
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    Player player;

    @Override
    public String getName() {
        return player.getName() + " - Xenon Settings";
    }

    @Override
    public String getTitle(Player player) {
        return player.getName() + " - Xenon Settings";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new Info_Button(player));
        buttons.put(11, new Reload_Config_Button());
        buttons.put(12, new Servers_Button());
        buttons.put(13, new Modules_Button());
        buttons.put(15, new Commands_Button());

        return buttons;
    }

    @AllArgsConstructor
    public static class Info_Button extends Button {
        Player player;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("SKULL_ITEM");
            builder.setData((short) 3);
            builder.setOwner(player.getName());
            builder.setDisplayName("&6Plugin Information");
            builder.setLore(Arrays.asList(
                    "&7Hello &6" + player.getName() + "&7,",
                    "&7Below you can find all plugin information.",
                    "&7",
                    "&6Uptime: &7" + TimeUtil.formatTimeMillis(System.currentTimeMillis() - XenonBukkit.getInstance().getUptime())
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }

    @AllArgsConstructor
    public static class Servers_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("REDSTONE_BLOCK");
            builder.setData((short) 0);
            builder.setOwner(player.getName());
            builder.setDisplayName("&6Server Manager");
            builder.setLore(Arrays.asList(
                    "&7This feature allows you to view",
                    "&7your active servers, and proxies.",
                    "&7",
                    "&6Left-Click &7to view more information."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                new ServerListMenu().openMenu(player);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }

    @AllArgsConstructor
    public static class Modules_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("BEACON");
            builder.setData((short) 0);
            builder.setOwner(player.getName());
            builder.setDisplayName("&6Reload Modules");
            builder.setLore(Arrays.asList(
                    "&7This feature allows you to reload",
                    "&7a specific module in the core.",
                    "&7",
                    "&6Left-Click &7to reload a specific module.",
                    "&6Right-Click &7to reload all modules."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                new ModulesMenu(player).openMenu(player);
            } else if (clickType.isRightClick()) {
                XenonBukkit.getInstance().getTaskUtil().runAsync(() -> {
                    for (ModuleLoader module : XenonBukkit.getInstance().getModules()) {
                        module.onReload();
                    }
                    player.sendMessage(CC.translate(CC.translate("&6[Xenon] &7You have successfully reload all &6Modules&7!")));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&6[Xenon] &c" + player.getName() + " &chas just reloaded all &6Modules&c!"));
                });
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }

    @AllArgsConstructor
    public static class Reload_Config_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("BOOK_AND_QUILL");
            builder.setData((short) 0);
            builder.setOwner(player.getName());
            builder.setDisplayName("&6Reload Config");
            builder.setLore(Arrays.asList(
                    "&7This feature allows you to reload",
                    "&7a specific config, or all of them at once.",
                    "&7",
                    "&6Left-Click &7to reload a specific config.",
                    "&6Right-Click &7to reload all configs."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                new ConfigReloadMenu(player).openMenu(player);
            } else if (clickType.isRightClick()) {
                XenonBukkit.getInstance().getTaskUtil().runAsync(() -> {
                    for (Config config : XenonBukkit.getInstance().getConfigList()) {
                        try {
                            config.save();
                            config.reload();
                        } catch (IOException | InvalidConfigurationException e) {
                            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

                            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                            bukkitCommandSender.sendMessage(CC.translate("&cXenon - Configuration Error"));
                            bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
                            bukkitCommandSender.sendMessage(CC.translate("&cError:"));
                            e.printStackTrace();
                            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                        }
                    }
                    XenonBukkit.getInstance().setupConfig();
                    player.sendMessage(CC.translate(CC.translate("&aYou have successfully reload all &lConfiguration Files&a!")));
                });
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }

    @AllArgsConstructor
    public static class Commands_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("PAPER");
            builder.setData((short) 0);
            builder.setOwner(player.getName());
            builder.setDisplayName("&6Manage Commands");
            builder.setLore(Arrays.asList(
                    "&7This feature allows you to enable or",
                    "&7disable commands whilst ingame.",
                    "&7",
                    "&6Left-Click &7to view the commands editor."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new CommandsMenu().openMenu(player);
        }
    }
}
