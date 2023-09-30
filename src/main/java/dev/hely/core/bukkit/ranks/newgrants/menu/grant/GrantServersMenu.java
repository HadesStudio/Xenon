package dev.hely.core.bukkit.ranks.newgrants.menu.grant;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantDurationMenu;
import dev.hely.core.bukkit.ranks.newgrants.promts.GrantServerPrompt;
import dev.hely.lib.callback.Callback;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class GrantServersMenu extends PaginatedMenu {

    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;
    private final List<String> servers;
    private final String type;

    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return "Grant Servers";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        getMenuContent().put(36, new BackButton(backMenu));
        getMenuContent().put(2, new GrantClearServersButton());
        getMenuContent().put(4, new AddServerButton());
        getMenuContent().put(6, new GrantInformationButton());

        for (String server : servers) {
            buttons.put(buttons.size(), new GrantServerButton(server));
        }

        return buttons;
    }

    public class GrantInformationButton extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            List<String> lore = Lists.newArrayList();

            lore.add("&7Below, is all the servers that");
            lore.add("&7this grant will be active on!");
            lore.add("&7");
            lore.add("&6Servers:");
            if(servers.isEmpty()) {
                lore.add(" &7- &6This grant is global..");
            }
            for(String server : servers) {
                lore.add(" &7- &c" + server);
            }

            lore.add(" ");

            return ItemMaker.of(Material.EMERALD_BLOCK).setDisplayName("&6Save Changes").setLore(lore).build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if(clickType.isLeftClick()) {
                new GrantDurationMenu(backMenu, profile, grant, servers, type).openMenu(player);
                Sound.AFFIRMATIVE.toPlayer(player);
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
            ItemMaker itemBuilder = ItemMaker.of(Material.NETHER_STAR);

            itemBuilder.setDisplayName("&6" + server);
            itemBuilder.setLore("", "&6Left-Click &7to remove this server.", "");

            return itemBuilder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if(clickType.isLeftClick()) {
                if (servers.contains(server)) {
                    servers.remove(server);
                }
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
            return ItemMaker.of(Material.FLINT_AND_STEEL).setDisplayName("&cClear Servers?").setLore("", "&6Left-Click &7to reset this grants servers.", "").build();
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
            return ItemMaker.of(Material.ANVIL).setDisplayName("&6Add Server").setLore("", "&6Left-Click &7to add a server to this grant.", "").build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();

            new GrantServerPrompt(servers, (Callback<Player>) data -> new GrantServersMenu(backMenu, profile, grant, servers, type).openMenu(player)).startPrompt(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}

