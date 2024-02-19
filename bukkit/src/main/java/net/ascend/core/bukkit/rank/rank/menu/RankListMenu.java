package net.ascend.core.bukkit.rank.rank.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.rank.menu
//   Date: Wednesday, July 05, 2023 - 2:02 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class RankListMenu extends PaginatedMenu {

    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public String getTitle(Player player) {
        return "&d&lRank List";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Rank rank : XenonBukkit.getInstance().getRankManager().getRanks()) {
            buttons.put(buttons.size(), new Rank_Button(rank));
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class Rank_Button extends Button {
        private final Rank rank;

        public String getMaterial() {
            return XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("menu.rank-list.rank-button.material");
        }

        public short getData() {
            if (XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getBoolean("menu.rank-list.rank-button.data.match-display-color")) {
                return ColorUtil.COLOR_MAP.get(rank.getDisplay_color()).getWoolData();
            } else {
                return (short) XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getInt("menu.rank-list.rank-button.data.data");
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(rank.getDisplay_color() + XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("menu.rank-list.rank-button.title").replace("%rank_name%", rank.getName()));
            for (String line : XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getStringList("menu.rank-list.rank-button.lore")) {
                builder.addLore(line
                        .replace("%rank_color%", rank.getDisplay_color().name().toUpperCase())
                        .replace("%rank_weight%", String.valueOf(rank.getWeight()))
                        .replace("%rank_suffix%", "&7'&c" + rank.getSuffix() + "&7'")
                        .replace("%rank_prefix%", "&7'&c" + rank.getPrefix() + "&7'")
                        .replace("%rank_inheritances%", String.valueOf(rank.getInheritances().size()))
                        .replace("%rank_permissions_size%", String.valueOf(rank.getPermissionsData().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new RankPermissionsMenu(rank).openMenu(player);
            } else {
                new RankInheritancesMenu(rank).openMenu(player);
            }
        }
    }
}
