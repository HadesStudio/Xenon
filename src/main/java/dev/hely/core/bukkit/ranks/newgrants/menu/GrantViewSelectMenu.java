package dev.hely.core.bukkit.ranks.newgrants.menu;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.menu.permission.PermissionGrantsMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.rank.RankGrantsMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.tag.TagGrantsMenu;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class GrantViewSelectMenu extends Menu {
    Profile profile;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.rows") * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.rank-grant.slot"), new RankGrantsButton(profile));
        buttons.put(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.permission-grant.slot"), new PermissionGrantsButton(profile));
        buttons.put(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.tag-grant.slot"), new TagGrantsMenu(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class RankGrantsButton extends Button {
        Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.rank-grant.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.rank-grant.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.rank-grant.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grantselect-menu-rank-grant-button' is null, using a default material."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grantselect-menu-rank-grant-button' is null, using a default material."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.rank-grant.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.rank-grant.display-name"));
            for (String line : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.selection-menu.buttons.rank-grant.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new RankGrantsMenu(profile).openMenu(player);
            }
        }
    }

    @AllArgsConstructor
    public static class PermissionGrantsButton extends Button {
        Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.permission-grant.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.permission-grant.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.permission-grant.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grantselect-menu-permission-grant-button' is null, using a default material."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grantselect-menu-permission-grant-button' is null, using a default material."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.permission-grant.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.permission-grant.display-name"));
            for (String line : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.selection-menu.buttons.permission-grant.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new PermissionGrantsMenu(profile).openMenu(player);
            }
        }
    }

    @AllArgsConstructor
    public static class TagGrantsMenu extends Button {
        Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.tag-grant.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.tag-grant.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.tag-grant.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grantselect-menu-tag-grant-button' is null, using a default material."));
                    return Material.NAME_TAG;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grantselect-menu-tag-grant-button' is null, using a default material."));
                return Material.NAME_TAG;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.tag-grant.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.tag-grant.display-name"));
            for (String line : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.selection-menu.buttons.tag-grant.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new dev.hely.core.bukkit.ranks.newgrants.menu.tag.TagGrantsMenu(profile).openMenu(player);
            }
        }
    }
}
