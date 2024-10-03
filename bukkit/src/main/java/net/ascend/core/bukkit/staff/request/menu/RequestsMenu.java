package net.ascend.core.bukkit.staff.request.menu;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.staff.request.menu.submenus.ClosedRequestsMenu;
import net.ascend.core.bukkit.staff.request.menu.submenus.ActiveRequestsMenu;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RequestsMenu extends Menu {

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("requests-menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("requests-menu.title");
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("requests-menu.buttons.requests.slot"), new Requests_Button());
        buttons.put(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("requests-menu.buttons.closed.slot"), new Closed_Button());

        return buttons;
    }

    @AllArgsConstructor
    public static class Requests_Button extends Button {

        public String getMaterial() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("requests-menu.buttons.requests.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("requests-menu.buttons.requests.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("requests-menu.buttons.requests.display_name"));

            for (String line : XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("requests-menu.buttons.requests.lore")) {
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
        public String getMaterial() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("requests-menu.buttons.closed.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("requests-menu.buttons.closed.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("requests-menu.buttons.closed.display_name"));

            for (String line : XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("requests-menu.buttons.closed.lore")) {
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
