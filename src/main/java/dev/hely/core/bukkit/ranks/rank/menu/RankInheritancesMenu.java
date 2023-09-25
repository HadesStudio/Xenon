package dev.hely.core.bukkit.ranks.rank.menu;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.ranks.rank.permission.PermissionData;
import dev.hely.lib.callback.Callback;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class RankInheritancesMenu extends PaginatedMenu {

    {
        setClickUpdate(true);
    }

    final Rank rank;

    @Override
    public String getName() {
        return "Rank Inheritances";
    }

    @Override
    public String getTitle(Player player) {
        return "Rank Inheritances";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.rank.getInheritances().forEach(rankName -> buttons.put(buttons.size(), new RankInheritancesButton(rank, rankName)));

        return buttons;
    }

    @AllArgsConstructor
    public static class RankInheritancesButton extends Button {
        private final Rank rank;
        private final String inheritanceRank;

        @Override
        public ItemStack getItemStack(Player player) {
            Rank inheritRank = OxygenBukkit.getInstance().getRankManager().getRank(inheritanceRank);

            ItemMaker builder = ItemMaker.of(inheritRank.isGlobalRank() ? Material.ENCHANTED_BOOK : Material.BOOK);
            builder.setDisplayName(inheritRank.getDisplay_color() + inheritRank.getName());

            builder.addLore("");
            builder.addLore("&6Global Rank&7: &a" + (inheritRank.isGlobalRank() ? "&aYes" : "&cNo"));
            builder.addLore("&6Servers&7: &a" + (inheritRank.getServers().isEmpty() ? "Global" : ""));
            for (String server : inheritRank.getServers()) {
                builder.addLore("&7- &f" + server);
            }
            builder.addLore("&6Active Permissions&7: &a" + inheritRank.getActivePermissions().size() + " Permissions");
            builder.addLore("&6Bungee Permissions&7: &a" + inheritRank.getBungeePermissions().size() + " Permissions");
            builder.addLore("");
            builder.addLore("&6Left-Click &7to view this inheritances permissions.");

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Rank inheritRank = OxygenBukkit.getInstance().getRankManager().getRank(inheritanceRank);

            new RankPermissionsMenu(inheritRank).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType == ClickType.LEFT || clickType == ClickType.MIDDLE;
        }
    }
}
