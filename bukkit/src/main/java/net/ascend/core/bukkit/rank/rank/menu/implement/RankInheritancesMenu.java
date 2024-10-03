package net.ascend.core.bukkit.rank.rank.menu.implement;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.callback.Callback;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
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

    private final Rank rank;

    @Override
    public String getName() {
        return "Rank Inheritances";
    }

    @Override
    public String getTitle(Player player) {
        return rank.getName() + " - Inheritances";
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
        menuContent.put(40, new AddedRankInheritanceButton(rank));
        menuContent.put(41, Button.getPlaceholder());

        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, new PageButton(1, this));
        menuContent.put(44, Button.getPlaceholder());

        return menuContent;
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
            Rank inheritRank = XenonBukkit.getInstance().getRankManager().getRank(inheritanceRank);

            ItemMaker builder = ItemMaker.of(inheritRank.isGlobalRank() ? "ENCHANTED_BOOK" : "BOOK");
            builder.setDisplayName(inheritRank.getDisplay_color() + inheritRank.getName());

            builder.addLore("");
            builder.addLore("&6Global Rank&7: &a" + (inheritRank.isGlobalRank() ? "&aYes" : "&cNo"));
            builder.addLore("&6Servers&7: &a" + (inheritRank.getServers().isEmpty() ? "Global" : ""));

            for (String server : inheritRank.getServers()) {
                builder.addLore("&7- &f" + server);
            }

            builder.addLore("&6Active Permissions&7: &a" + inheritRank.getPermissionsData().size() + " Permissions");
            builder.addLore("");
            builder.addLore("&6Left-Click &7to view this inheritances permissions.");

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Rank inheritRank = XenonBukkit.getInstance().getRankManager().getRank(inheritanceRank);

            new RankPermissionsMenu(inheritRank).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType == ClickType.LEFT || clickType == ClickType.MIDDLE;
        }
    }


    @AllArgsConstructor
    public static class AddedRankInheritanceButton extends Button {

        private final Rank rank;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("BOOK");
            builder.setDisplayName("&aAdded Inheritace Rank");

            builder.addLore("");
            builder.addLore("&6Active Inheritace&7: &a" + rank.getServers().size());

            for (String server : rank.getServers()) {
                builder.addLore("&7- &f" + server);
            }

            builder.addLore("");
            builder.addLore("&7Left-Click to added new inheritance.");

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();

            conversationString(player, "&eType the new inheritance:", inheritance -> {
                for (String inheritances : rank.getInheritances()) {
                    if (inheritances.equalsIgnoreCase(inheritance)) {
                        player.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.already-inherit")));

                        new RankInheritancesMenu(rank).openMenu(player);
                        return;
                    }
                }

                Rank inheritRank = XenonBukkit.getInstance().getRankManager().getRank(inheritance);
                if (inheritRank == null) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
                    new RankInheritancesMenu(rank).openMenu(player);
                    return;
                }


                player.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.inherit.added-inherit")
                        .replace("%rank_name%", rank.getName())
                        .replace("%inherit_rank_name%", inheritance)));

                rank.getInheritances().add(inheritRank.getName());
                rank.save();

                XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(rank.getName()));

                new RankInheritancesMenu(rank).openMenu(player);
            });
        }
    }

    private static void conversationString(Player p, String prompt, Callback<String> callback) {
        ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return prompt;
            }

            public Prompt acceptInput(ConversationContext cc, String newName) {
                callback.callback(newName);
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("quit").withTimeout(10).thatExcludesNonPlayersWithMessage("how does console have this?? what m88888");

        Conversation con = factory.buildConversation(p);
        p.beginConversation(con);
    }
}
