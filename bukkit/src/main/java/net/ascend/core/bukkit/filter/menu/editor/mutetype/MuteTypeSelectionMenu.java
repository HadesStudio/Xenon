package net.ascend.core.bukkit.filter.menu.editor.mutetype;

import net.ascend.core.bukkit.filter.Filter;
import net.ascend.core.bukkit.filter.menu.editor.FilterEditorMenu;
import net.ascend.core.bukkit.filter.types.MuteType;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MuteTypeSelectionMenu extends PaginatedMenu {

    private final Filter filter;

    @Override
    public String getName() {
        return filter.getName() + " - Mute Type Editor";
    }

    @Override
    public String getTitle(Player player) {
        return filter.getName() + " - Mute Type Editor";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (MuteType muteType : MuteType.values()) {
            buttons.put(buttons.size(), new Mute_Mode_Edit(filter, muteType));
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
    public static class Mute_Mode_Edit extends Button {
        Filter filter;
        MuteType type;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("PAPER");
            builder.setData((short) 0);
            builder.setDisplayName("&6" + type.getDisplay());
            builder.setLore("&7" + type.getDescription());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            filter.setMuteType(type);
            filter.save();

            player.sendMessage(CC.translate("&aYou have successfully updated this filter's mute type to &l" + type.getDisplay() + "&a."));
            player.closeInventory();
            new FilterEditorMenu(filter).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }
}
