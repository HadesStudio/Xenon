package net.ascend.core.bukkit.rank.rank.menu;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.implement.RankInheritancesMenu;
import net.ascend.core.bukkit.rank.rank.menu.implement.RankPermissionsMenu;
import net.ascend.core.bukkit.rank.rank.menu.implement.RankServerMenu;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
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
        return "&8Rank List";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        XenonBukkit.getInstance().getRankManager().getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight))
                .forEach(rank -> buttons.put(buttons.size(), new RankButton(rank)));
        return buttons;
    }

    @AllArgsConstructor
    public static class RankButton extends Button {

        private final Rank rank;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("LEATHER_CHESTPLATE");
            builder.setColor(ColorUtil.getColorFromChatColor(rank.getDisplay_color()));

            builder.setDisplayName(rank.getDisplay_color() + rank.getName());
            builder.setLore("&7&l&m-----------------------------------------",
                    "&6Type: &f" + rank.getType(),
                    "&6Prefix: &f" + rank.getPrefix(),
                    "&6Suffix: &f" + rank.getSuffix(),
                    "&6Weight: &f" + rank.getWeight(),
                    "&6Color: &f" + rank.getDisplay_color().name().toUpperCase(),
                    "&6Inheritances: &f" + rank.getInheritances().size(),
                    "&6Permissions: &f" + rank.getPermissionsData().size(),
                    "",
                    "&7Left-Click to view full permissions of this rank.",
                    "&7Middle-Click to view this rank's server-specific settings.",
                    "&7Right-Click to view this ranks inheritances.",
                    "&7&l&m-----------------------------------------");


            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new RankPermissionsMenu(rank).openMenu(player);
            } else if (clickType == ClickType.RIGHT) {
                new RankInheritancesMenu(rank).openMenu(player);
            } else if (clickType == ClickType.MIDDLE) {
                new RankServerMenu(rank).openMenu(player);
            }
        }
    }
}
