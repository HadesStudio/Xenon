package net.ascend.core.bukkit.rank.rank.menu.implement;

import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.callback.Callback;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.server.Server;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class RankServerMenu extends PaginatedMenu {

    {
        setClickUpdate(true);
    }

    private final Rank rank;

    @Override
    public String getName() {
        return "Rank Server";
    }

    @Override
    public String getTitle(Player player) {
        return rank.getName() + " - Servers";
    }

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


        menuContent.put(36, Button.getPlaceholder());
        menuContent.put(37, new PageButton(-1, this));
        menuContent.put(38, Button.getPlaceholder());

        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, new GlobalRankButton());
        menuContent.put(41, Button.getPlaceholder());

        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, new PageButton(1, this));
        menuContent.put(44, Button.getPlaceholder());

        return menuContent;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        XenonBukkit.getInstance().getServerManager().getServers().forEach(server -> {
            buttons.put(buttons.size(), new RankServerButton(server.getName()));
        });


        return buttons;
    }

    @AllArgsConstructor
    private class RankServerButton extends Button {

        private final String server;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(rank.isGlobalRank() ? "REDSTONE_BLOCK" : "PAPER");
            builder.setDisplayName(rank.getDisplay_color() + rank.getName() + " &7- &f" + this.server);

            builder.addLore("");

            if (!rank.isGlobalRank()) {
                if (rank.isOnServer(this.server)) {
                    builder.addLore("&7- &aYes");
                    builder.addLore("&cNo");
                } else {
                    builder.addLore("&cYes");
                    builder.addLore("&7-  &aNo");
                }
            }

            if (rank.isGlobalRank()) {
                builder.addLore("&cYou can't toggle this because");
                builder.addLore("&crank is currently &lGlobal&c.");
            }

            builder.addLore("");
            builder.addLore("&7Left-Click to toggle this server's");

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                if (rank.isGlobalRank()) return;

                if (rank.getServer(this.server)) {
                    rank.removeServer(this.server);
                    rank.save();
                } else {
                    rank.getServers().add(this.server);
                    rank.save();
                }

                XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(rank.getName()));
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType == ClickType.LEFT;
        }
    }


    @AllArgsConstructor
    private class GlobalRankButton extends Button {

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(rank.isGlobalRank() ? "ENCHANTED_BOOK" : "BOOK");
            builder.setDisplayName("&aGlobal Rank?");

            builder.addLore("");
            builder.addLore("&6Global&7: &f" + (rank.isGlobalRank() ? "&aYes" : "&cNo"));
            builder.addLore("");
            builder.addLore("&7Left-Click to toggle global status.");

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                rank.setGlobalRank(!rank.isGlobalRank());
                rank.save();

                XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(rank.getName()));
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType == ClickType.LEFT;
        }
    }
}
