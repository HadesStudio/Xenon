package dev.hely.core.bukkit.server.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.menu
//   Date: Wednesday, August 16, 2023 - 10:23 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.core.menus.OxygenMenu;
import dev.hely.core.bukkit.core.menus.buttons.InfoButton;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.utils.TPSUtil;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class ServerListMenu extends PaginatedMenu {
    {
        setClickUpdate(true);
    }
    Player player;

    @Override
    public String getName() {
        return player.getName() + " - Server List";
    }

    @Override
    public String getTitle(Player player) {
        return player.getName() + " - Server List";
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

        for (Server servers : OxygenBukkit.getInstance().getServerManager().getServers()) {
            buttons.put(buttons.size(), new ServerButton(servers));
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
    public static class ServerButton extends Button {
        Server server;

        public Material getMaterial() {
            if(server.getStatus().equalsIgnoreCase("Offline")) return Material.REDSTONE_BLOCK;
            if(server.getStatus().equalsIgnoreCase("Whitelisted")) return Material.GOLD_BLOCK;
            if(server.getStatus().equalsIgnoreCase("Online")) return Material.EMERALD_BLOCK;
            return null;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData((short) 0);
            builder.setDisplayName("&6Server - " + server.getName() + " &7(&c" + server.getStatus() + "&7)");
            builder.setLore(Arrays.asList(
                    "&7Below, is a few options you have",
                    "&7to modify and make changes to this",
                    "&7server.",
                    "&7",
                    "&6Server Information:",
                    " &6- TPS: &c" + TPSUtil.getTPSStatus(server.getLastTps()),
                    " &6- Players: &c" + server.getOnlinePlayers() + "&7/&c" + server.getMaximumPlayers(),
                    " &6- Status: &c" + server.getStatus(),
                    " &6- Start Time: &c" + TimeUtil.formatIntoCalendarString(new Date(server.getStartupTime())),
                    " &6- Uptime: &c" + TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getStartupTime()),
                    " &6- Last Haertbeat: &c" + TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getLastHeartBeat()),
                    "&7",
                    "&6Left-Click &7to view more information.",
                    "&6Middle-Click &7to connect to this server.",
                    "&6Right-Click &7to shutdown this server."
            ));
            builder.addFakeGlow();
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
            } else if (clickType.isRightClick()) {
            }
        }
    }
}

