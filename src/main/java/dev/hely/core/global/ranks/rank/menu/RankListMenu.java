package dev.hely.core.global.ranks.rank.menu;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.global.ranks.rank.Rank;
import dev.hely.core.utils.material.ColorUtil;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

        for (Rank rank : OxygenBukkit.getInstance().getRankManager().getRanks()) {
            buttons.put(buttons.size(), new Rank_Button(rank));
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class Rank_Button extends Button {
        private final Rank rank;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("menu.rank-list.rank-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("menu.rank-list.rank-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("menu.rank-list.rank-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'rank-button' is null, using a default material."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'rank-button' is broken, using a default material."));
                return Material.WOOL;
            }
        }

        public short getData() {
            if (OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getBoolean("menu.rank-list.rank-button.data.match-display-color")) {
                return ColorUtil.COLOR_MAP.get(rank.getDisplay_color()).getWoolData();
            } else {
                return (short) OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getInt("menu.rank-list.rank-button.data.data");
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(rank.getDisplay_color() + OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("menu.rank-list.rank-button.title").replace("%rank_name%", rank.getName()));
            for (String line : OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getStringList("menu.rank-list.rank-button.lore")) {
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
        }
    }
}
