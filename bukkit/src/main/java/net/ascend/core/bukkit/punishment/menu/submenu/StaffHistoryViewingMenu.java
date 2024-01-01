package net.ascend.core.bukkit.punishment.menu.submenu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.punishment.menu.submenu
//   Date: Wednesday, September 06, 2023 - 7:21 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.PageButton;
import net.ascend.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class StaffHistoryViewingMenu extends PaginatedMenu {
    final Profile profile;
    final List<Punishment> punishments;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("viewing-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("viewing-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Punishment punishment : punishments) {
            buttons.put(buttons.size(), new Punishment_Button(profile, punishment));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
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
    public static class Punishment_Button extends Button {
        final Profile profile;
        final Punishment punishment;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("viewing-menu.buttons.punishment-button.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("viewing-menu.buttons.punishment-button.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("viewing-menu.buttons.punishment-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'staff-history-viewing-punishment-material' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'staff-history-viewing-punishment-material' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("viewing-menu.buttons.punishment-button.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("viewing-menu.buttons.punishment-button.display-name").replace("%type%", punishment.getType().name()));
            for (String line : XenonBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getStringList("viewing-menu.buttons.punishment-button.lore")) {
                builder.addLore(line
                        .replace("%date%", TimeUtil.formatSimplifiedDateDiff(punishment.getAddedAt()))
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%target%", XenonBukkit.getInstance().getProfileManager().getProfile(punishment.getTargetUUID()).getName())
                        .replace("%duration%", TimeUtil.getMoreDetailedTime(punishment.getDuration()))
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


