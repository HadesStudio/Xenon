package net.ascend.core.bukkit.rank.grant.menu.grant;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.grant.rank.RankGrantMenu;
import net.ascend.core.bukkit.rank.grant.promts.GrantPermissionPrompt;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.Menu;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class GrantMenu extends Menu {
    Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public int getSize() {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.rows") * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.rank-grant.slot"), new RankGrantsButton(profile));
        buttons.put(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.permission-grant.slot"), new PermissionGrantsButton(profile));
        buttons.put(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.tag-grant.slot"), new TagGrantsMenu(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class RankGrantsButton extends Button {
        Profile profile;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.rank-grant.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.rank-grant.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.rank-grant.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'grantselect-menu-rank-grant-button' is null, using a default material."));
                    return MaterialUtil.WOOL.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'grantselect-menu-rank-grant-button' is null, using a default material."));
                return MaterialUtil.WOOL.getMaterial();
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.rank-grant.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.rank-grant.display-name"));
            for (String line : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.selection-menu.buttons.rank-grant.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new RankGrantMenu(profile).openMenu(player);
            }
        }
    }

    @AllArgsConstructor
    public static class PermissionGrantsButton extends Button {
        Profile profile;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.permission-grant.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.permission-grant.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.permission-grant.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'grantselect-menu-permission-grant-button' is null, using a default material."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'grantselect-menu-permission-grant-button' is null, using a default material."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.permission-grant.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.permission-grant.display-name"));
            for (String line : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.selection-menu.buttons.permission-grant.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                player.closeInventory();
                new GrantPermissionPrompt(profile).startPrompt(player);
            }
        }
    }

    @AllArgsConstructor
    public static class TagGrantsMenu extends Button {
        Profile profile;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.tag-grant.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.tag-grant.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.tag-grant.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'grantselect-menu-tag-grant-button' is null, using a default material."));
                    return Material.NAME_TAG;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'grantselect-menu-tag-grant-button' is null, using a default material."));
                return Material.NAME_TAG;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getInt("menus.selection-menu.buttons.tag-grant.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.selection-menu.buttons.tag-grant.display-name"));
            for (String line : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.selection-menu.buttons.tag-grant.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new net.ascend.core.bukkit.rank.grant.menu.tag.TagGrantsMenu(profile).openMenu(player);
            }
        }
    }
}
