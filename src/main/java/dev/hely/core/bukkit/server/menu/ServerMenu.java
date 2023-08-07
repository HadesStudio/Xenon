package dev.hely.core.bukkit.server.menu;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.staff.menu.RequestsMainMenu;
import dev.hely.core.bukkit.staff.menu.submenus.ActiveRequestsMenu;
import dev.hely.core.bukkit.staff.menu.submenus.ClosedRequestsMenu;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 06/08/2023
 * Protect Name Oxygen
 */
public class ServerMenu extends PaginatedMenu {
    {
        setPlaceholder(true);
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Server server : OxygenBukkit.getInstance().getServerManager().getServers()) {

        }

        return buttons;
    }

    @AllArgsConstructor
    public static class Server_Button extends Button {
        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.requests.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.requests.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.requests.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'requests-button' is null, using a default material."));
                    return Material.NETHER_STAR;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'requests-button' is broken, using a default material."));
                return Material.NETHER_STAR;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("main-menu.buttons.requests.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.requests.display-name"));

            for (String line : OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("main-menu.buttons.requests.lore")) {
                builder.addLore(line
                        .replace("%request_size%", String.valueOf(OxygenBukkit.getInstance().getRequestHandler().generateOpenRequestInteger())
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
            if (OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.closed.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.closed.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.closed.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'closed-button' is null, using a default material."));
                    return Material.REDSTONE_BLOCK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'closed-button' is broken, using a default material."));
                return Material.REDSTONE_BLOCK;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("main-menu.buttons.closed.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("main-menu.buttons.closed.display-name"));

            for (String line : OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("main-menu.buttons.closed.lore")) {
                builder.addLore(line
                        .replace("%request_size%", String.valueOf(OxygenBukkit.getInstance().getRequestHandler().generateClosedRequestInteger())
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
