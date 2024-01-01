package net.ascend.core.bukkit.staff.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.staff.menu
//   Date: Sunday, July 30, 2023 - 5:59 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.staff.menu.submenus.ClosedRequestsMenu;
import net.ascend.core.bukkit.staff.menu.submenus.ActiveRequestsMenu;
import net.ascend.lib.menu.fill.FillerTemplate;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.Menu;
import net.ascend.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RequestsMainMenu extends Menu {
    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.title");
    }

    @Override
    public int getSize() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("main-menu.size");
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("main-menu.buttons.requests.slot"), new Requests_Button());
        buttons.put(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("main-menu.buttons.closed.slot"), new Closed_Button());

        return buttons;
    }

    @AllArgsConstructor
    public static class Requests_Button extends Button {
        public Material getMaterial() {
            if (XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.requests.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.requests.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.requests.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'requests-button' is null, using a default material."));
                    return Material.NETHER_STAR;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'requests-button' is broken, using a default material."));
                return Material.NETHER_STAR;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("main-menu.buttons.requests.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.requests.display-name"));

            for (String line : XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("main-menu.buttons.requests.lore")) {
                builder.addLore(line
                        .replace("%request_size%", String.valueOf(XenonBukkit.getInstance().getRequestHandler().generateOpenRequestInteger())
                ));
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new ActiveRequestsMenu().openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class Closed_Button extends Button {
        public Material getMaterial() {
            if (XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.closed.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.closed.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.closed.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'closed-button' is null, using a default material."));
                    return Material.REDSTONE_BLOCK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'closed-button' is broken, using a default material."));
                return Material.REDSTONE_BLOCK;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("main-menu.buttons.closed.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.closed.display-name"));

            for (String line : XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("main-menu.buttons.closed.lore")) {
                builder.addLore(line
                        .replace("%request_size%", String.valueOf(XenonBukkit.getInstance().getRequestHandler().generateClosedRequestInteger())
                        ));
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new ClosedRequestsMenu().openMenu(player);
        }
    }
}
