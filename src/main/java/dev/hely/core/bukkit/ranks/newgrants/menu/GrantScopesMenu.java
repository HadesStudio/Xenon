package dev.hely.core.bukkit.ranks.newgrants.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantServersMenu;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.fill.FillerTemplate;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class GrantScopesMenu extends Menu {
    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;
    private final String type;

    @Override
    public String getTitle(Player player) {
        return "Select Type";
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return "Select Type";
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(4, new BackButton(backMenu));

        if (grant instanceof PermissionGrant) {
            PermissionGrant permissionGrant = (PermissionGrant) grant;

            buttons.put(11, new Button() {

                @Override
                public ItemStack getItemStack(Player player) {
                    return ItemMaker.of(Material.QUARTZ).setDisplayName("&6BungeeCord Rank").setLore("", "&6Left-Click &7to set this rank as a bungeecord rank.", "").build();
                }

                @Override
                public void onClick(Player player, int slot, ClickType clickType) {
                    permissionGrant.setBungeecord(true);

                    new GrantDurationMenu(GrantScopesMenu.this, profile, permissionGrant, Lists.newArrayList(), type).openMenu(player);
                    Sound.AFFIRMATIVE.toPlayer(player);
                }
            });

            buttons.put(13, new Button() {

                @Override
                public ItemStack getItemStack(Player player) {
                    return ItemMaker.of(Material.GOLD_INGOT).setDisplayName("&9Bukkit Rank").setLore("", "&6Left-Click &7to set this rank as a bukkit rank.", "").build();
                }

                @Override
                public void onClick(Player player, int slot, ClickType clickType) {
                    permissionGrant.setBungeecord(false);

                    new GrantDurationMenu(GrantScopesMenu.this, profile, permissionGrant, Lists.newArrayList(), type).openMenu(player);
                    Sound.AFFIRMATIVE.toPlayer(player);
                }
            });
        } else {
            buttons.put(11, new Button() {

                @Override
                public ItemStack getItemStack(Player player) {
                    return ItemMaker.of(Material.EMERALD).setDisplayName("&6Global Rank").setLore("", "&6Left-CLick &7to set this rank as a global rank.", "").build();
                }

                @Override
                public void onClick(Player player, int slot, ClickType clickType) {
                    new GrantDurationMenu(GrantScopesMenu.this, profile, grant, Lists.newArrayList(), type).openMenu(player);
                    Sound.AFFIRMATIVE.toPlayer(player);
                }
            });
        }

        buttons.put(15, new Button() {
            @Override
            public ItemStack getItemStack(Player player) {
                return ItemMaker.of(Material.SIGN).setDisplayName("&6Setup Servers").setLore("", "&6Left-Click &7to setup specific servers for this rank.", "").build();
            }

            @Override
            public void onClick(Player player, int slot, ClickType clickType) {
                new GrantServersMenu(GrantScopesMenu.this, profile, grant, Lists.newArrayList(), type).openMenu(player);
                Sound.AFFIRMATIVE.toPlayer(player);
            }
        });

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}

