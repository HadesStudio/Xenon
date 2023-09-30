package dev.hely.core.bukkit.ranks.newgrants.menu;

import com.google.common.collect.Maps;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.promts.GrantDurationPrompt;
import dev.hely.core.bukkit.ranks.newgrants.promts.GrantReasonPrompt;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GrantDurationMenu extends Menu {
    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;
    private final List<String> servers;
    private final String type;

    @Override
    public String getTitle(Player player) {
        return "Select Duration";
    }

    @Override
    public String getName() {
        return "Select Duration";
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(4, new BackButton(new GrantScopesMenu(backMenu, profile, grant, type)));

        buttons.put(11, new GrantPermanentDurationButton(profile, grant, servers, type));
        buttons.put(15, new GrantCustomDurationButton(profile, grant, servers, type));

        return buttons;
    }

    @Override
    public int getSize() {
        return 27;
    }

    @AllArgsConstructor
    public static class GrantPermanentDurationButton extends Button {
        private final Profile profile;
        private final Grant grant;
        private final List<String> servers;
        private final String type;

        @Override
        public ItemStack getItemStack(Player player) {
            return ItemMaker.of(Material.WATCH).setDisplayName("&6Permanent Grant").setLore(" ", "&6Left-Click &7to grant this forever!", " ")
                    .build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                player.closeInventory();

                grant.setAddedBy(player.getUniqueId());
                grant.getServers().addAll(servers);
                grant.setDuration(Integer.MAX_VALUE);
                grant.setType(type);

                new GrantReasonPrompt(profile, grant).startPrompt(player);
            }
        }
    }

    @AllArgsConstructor
    public static class GrantCustomDurationButton extends Button {
        private final Profile profile;
        private final Grant grant;
        private final List<String> servers;
        private final String type;

        @Override
        public ItemStack getItemStack(Player player) {
            return ItemMaker.of(Material.BOOK_AND_QUILL).setDisplayName("&6Specify Custom Duration")
                    .setLore(" ", "&6Left-Click &7to specify a specific grant duration.", " ").build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                player.closeInventory();

                grant.setAddedBy(player.getUniqueId());
                grant.getServers().addAll(servers);
                grant.setType(type);

                new GrantDurationPrompt(profile, grant).startPrompt(player);
            }
        }
    }
}