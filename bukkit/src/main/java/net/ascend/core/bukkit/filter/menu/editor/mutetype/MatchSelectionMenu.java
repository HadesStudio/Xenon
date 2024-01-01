package net.ascend.core.bukkit.filter.menu.editor.mutetype;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.filter.menu.editor.mutetype
//   Date: Thursday, September 14, 2023 - 1:53 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.filter.Filter;
import net.ascend.core.bukkit.filter.menu.editor.FilterEditorMenu;
import net.ascend.core.bukkit.filter.types.Match;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.BackButton;
import net.ascend.lib.menu.button.implement.PageButton;
import net.ascend.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MatchSelectionMenu extends PaginatedMenu {
    final Filter filter;

    @Override
    public String getName() {
        return filter.getName() + " - Match Type Editor";
    }

    @Override
    public String getTitle(Player player) {
        return filter.getName() + " - Match Type Editor";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Match match : Match.values()) {
            buttons.put(buttons.size(), new Match_Edit(filter, match));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new BackButton(new FilterEditorMenu(filter)));
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
    public static class Match_Edit extends Button {
        Filter filter;
        Match match;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.PAPER);
            builder.setData((short) 0);
            builder.setDisplayName("&6" + match.getDisplay());
            builder.setLore("&7" + match.getDescription());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            filter.setMatch(match);
            filter.save();

            player.sendMessage(CC.translate("&6[Filter] &7You have successfully updated this filter's match type to &6" + match.getDisplay() + "&7."));
            player.closeInventory();
            new FilterEditorMenu(filter).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }
}
