package dev.hely.core.bukkit.server.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.menu.submenus
//   Date: Wednesday, August 16, 2023 - 11:52 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.core.menus.OxygenMenu;
import dev.hely.core.bukkit.core.menus.submenus.CommandsMenu;
import dev.hely.core.bukkit.core.menus.submenus.ConfigReloadMenu;
import dev.hely.core.bukkit.core.menus.submenus.ModulesMenu;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.server.menu.ServerListMenu;
import dev.hely.core.bukkit.utils.Config;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.server.data.server.data.StatusData;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InformationMenu extends Menu {
    {
        setPlaceholder(true);
        setClickUpdate(true);
    }
    
    Server server;

    @Override
    public String getName() {
        return server.getName() + " - Information";
    }

    @Override
    public String getTitle(Player player) {
        return server.getName() + " - Information";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new Restart_Button(server));

        return buttons;
    }

    @AllArgsConstructor
    public static class Restart_Button extends Button {
        Server server;

        public Material getMaterial() {
            if(server.getStatus() == StatusData.OFFLINE) return Material.REDSTONE_BLOCK;
            if(server.getStatus() == StatusData.WHITELISTED) return Material.GOLD_BLOCK;
            if(server.getStatus() == StatusData.ONLINE) return Material.EMERALD_BLOCK;
            return null;
        }
        public String getDisplayName() {
            if (server.getStatus() == StatusData.OFFLINE) return CC.translate("&cOffline");
            if (server.getStatus() == StatusData.WHITELISTED) return CC.translate("&dWhitelisted");
            if (server.getStatus() == StatusData.ONLINE) return CC.translate("&aOnline");
            return "Unknown?";
        }

        public List<String> getLore() {
            List<String> lore = new ArrayList<>();

            if (server.getStatus() == StatusData.OFFLINE) {
                lore.clear();
                lore.add("&7This server is currently &coffline&7.");
                return lore;
            } else if (server.getStatus() == StatusData.WHITELISTED) {
                lore.clear();
                lore.add("&7This server is currently &dwhitelisted&7.");
                lore.add("&7");
                lore.add("&6Left-Click &7to toggle the whitelist.");
                return lore;
            } else {
                lore.clear();
                lore.add("&7This server is currently &aonline&7.");
                lore.add("&7");
                lore.add("&6Left-Click &7to shutdown this server.");
                lore.add("&6Right-Click &7to toggle the whitelist.");
                return lore;
            }
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setDisplayName(getDisplayName());
            builder.setLore(getLore());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (server.getStatus() == StatusData.ONLINE) {
                if (clickType.isLeftClick()) {
                    
                } else {
                    server.setStatus(StatusData.WHITELISTED);
                    player.sendMessage(CC.translate("&aYou have successfully enabled the &dWhitelist &aon &6" + server.getName() + " Server&a."));
                }
                return;
            } else if (server.getStatus() == StatusData.WHITELISTED) {
                if (server.getStatus() == StatusData.WHITELISTED) {
                    server.setStatus(StatusData.ONLINE);
                    player.sendMessage(CC.translate("&aYou have successfully disabled the &dWhitelist &aon &6" + server.getName() + " Server&a."));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }
}

