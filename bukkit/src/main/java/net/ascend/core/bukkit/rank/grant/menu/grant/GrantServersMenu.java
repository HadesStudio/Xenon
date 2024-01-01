package net.ascend.core.bukkit.rank.grant.menu.grant;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.GrantDurationMenu;
import net.ascend.core.bukkit.rank.grant.promts.GrantServerPrompt;
import net.ascend.lib.callback.Callback;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.Menu;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.BackButton;
import net.ascend.lib.menu.button.implement.PageButton;
import net.ascend.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class GrantServersMenu extends PaginatedMenu {
    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;
    private final List<String> servers;

    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-servers.title");
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-servers.title");
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, new GrantClearServersButton());

        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, new AddServerButton());
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, new GrantInformationButton());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(9, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());
        menuContent.put(18, Button.getPlaceholder());
        menuContent.put(26, Button.getPlaceholder());
        menuContent.put(27, Button.getPlaceholder());
        menuContent.put(35, Button.getPlaceholder());

        menuContent.put(36, new BackButton(backMenu));
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

        for (String server : servers) {
            buttons.put(buttons.size(), new GrantServerButton(server));
        }

        return buttons;
    }

    public class GrantInformationButton extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.EMERALD_BLOCK);
            itemMaker.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-servers.buttons.information.display-name"));
            for (String lore : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.grant-servers.buttons.information.lore")) {
                itemMaker.addLore(lore.replace("%global_status%", isGlobal()).replace("%servers_size%", String.valueOf(servers.size())));
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new GrantDurationMenu(backMenu, profile, grant, servers).openMenu(player);
            Sound.AFFIRMATIVE.toPlayer(player);
        }

        public String isGlobal() {
            if (servers.isEmpty()) {
                return "&aYes";
            } else {
                return "&cNo";
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    @AllArgsConstructor
    public class GrantServerButton extends Button {
        String server;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.PAPER);
            itemMaker.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-servers.buttons.server.display-name").replace("%name%", server));
            for (String lore : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.grant-servers.buttons.server.lore")) {
                itemMaker.addLore(lore);
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if(clickType.isLeftClick()) {
                servers.remove(server);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    public class GrantClearServersButton extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.FLINT_AND_STEEL);
            itemMaker.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-servers.buttons.clear-servers.display-name"));
            for (String lore : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.grant-servers.buttons.clear-servers.lore")) {
                itemMaker.addLore(lore.replace("%servers_size%", String.valueOf(servers.size())));
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            servers.clear();
            Sound.NEGATIVE.toPlayer(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    public class AddServerButton extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.ANVIL);
            itemMaker.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-servers.buttons.add-server.display-name"));
            for (String lore : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.grant-servers.buttons.add-server.lore")) {
                itemMaker.addLore(lore.replace("%servers_size%", String.valueOf(servers.size())));
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();

            new GrantServerPrompt(servers, (Callback<Player>) data -> new GrantServersMenu(backMenu, profile, grant, servers).openMenu(player)).startPrompt(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}

