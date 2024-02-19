package net.ascend.core.bukkit.rank.rank.menu.perks;

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

import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.perk.Perk;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class RankPerksMenu extends PaginatedMenu {
    Rank rank;

    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public String getTitle(Player player) {
        return rank.getDisplay_color() + rank.getName() + " Perks";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Perk perk : rank.getPerks()) {
            buttons.put(buttons.size(), new Rank_Button(rank, perk));
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class Rank_Button extends Button {
        Rank rank;
        Perk perk;

        @Override
        public ItemStack getItemStack(Player player) {
            Integer data = perk.getMaterialData();

            return ItemMaker.of(perk.getMaterial()).setData(data.shortValue()).setDisplayName(perk.getDisplayName()).setLore(perk.getLore()).build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (perk.getClickMessage() != null) {
                for (String message : perk.getClickMessage()) {
                    player.sendMessage(CC.translate(message));
                }
            }
        }
    }
}
