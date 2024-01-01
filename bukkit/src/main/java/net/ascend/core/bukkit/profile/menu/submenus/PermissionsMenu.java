package net.ascend.core.bukkit.profile.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.menu.submenus
//   Date: Tuesday, August 01, 2023 - 4:07 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.menu.EditProfileMenu;
import net.ascend.core.bukkit.profile.permission.ProfilePermission;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.BackButton;
import net.ascend.lib.menu.button.implement.PageButton;
import net.ascend.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PermissionsMenu extends PaginatedMenu {
    final Profile profile;
    final String viewingType;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("permission-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("permission-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (viewingType.equalsIgnoreCase("bungee")) {
            for (String permission : profile.getBungeePermissions()) {

            }
        } else if (viewingType.equalsIgnoreCase("profile")) {
            for (ProfilePermission permission : profile.getProfilePermissions()) {
                buttons.put(buttons.size(), new Profile_Permission_Button(profile, permission));
            }
        } else { // view all
            for (ProfilePermission profilePermissions : profile.getProfilePermissions()) {
                buttons.put(buttons.size(), new Profile_Permission_Button(profile, profilePermissions));
            }
            for (String bungeePermissions : profile.getBungeePermissions()) {

            }
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new BackButton(new EditProfileMenu(profile)));
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());
        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());
        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());
        menuContent.put(9, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());
        menuContent.put(18, new PageButton(-1, this));
        menuContent.put(19, Button.getPlaceholder());
        menuContent.put(20, Button.getPlaceholder());
        menuContent.put(21, Button.getPlaceholder());
        menuContent.put(22, Button.getPlaceholder());
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());
        menuContent.put(26, new PageButton(1, this));
        return menuContent;
    }

    @AllArgsConstructor
    public static class Profile_Permission_Button extends Button {
        final Profile profile;
        final ProfilePermission perm;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("permission-menu.buttons.permission-button.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("permission-menu.buttons.permission-button.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("permission-menu.buttons.permission-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'permission-view-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'permission-view-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("permission-menu.buttons.permission-button.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("permission-menu.buttons.permission-button.display-name").replace("%permission%", perm.getPermission()).replace("%status%", String.valueOf(perm.isActive())));
            for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("permission-menu.buttons.permission-button.lore")) {
                builder.addLore(line
                        .replace("%expires%", TimeUtil.formatSimplifiedDateDiff(perm.getDuration()))
                        .replace("%status%", String.valueOf(perm.isActive()))
                        .replace("%server%", perm.getServer())
                        .replace("%type%", "Profile Permission")
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }
}
