package net.ascend.core.bukkit.rank.rank.menu.perks;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
// Ascend LLC Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: net.ascend.core.bukkit.rank.rank.menu.perks
//   Date: Sunday, January 21, 2024 - 8:22 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//   Ascend LLC Discord: https://discord.gg/xM8bktgC6q
//============================================================

import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.perks.impl.RankPerkEditSubMenu;
import net.ascend.core.bukkit.rank.rank.perk.Perk;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@AllArgsConstructor
public class RankPerkEditMenu extends PaginatedMenu {
    Rank rank;

    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public String getTitle(Player player) {
        return rank.getDisplay_color() + rank.getName() + " Perks";
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());

        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, new NewPerkButton(rank));
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(9, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());
        menuContent.put(18, Button.getPlaceholder());
        menuContent.put(26, Button.getPlaceholder());
        menuContent.put(27, Button.getPlaceholder());
        menuContent.put(35, Button.getPlaceholder());

        menuContent.put(36, Button.getPlaceholder());
        menuContent.put(37, new PageButton(-1, this));
        menuContent.put(38, Button.getPlaceholder());

        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, Button.getPlaceholder());
        menuContent.put(41, Button.getPlaceholder());

        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, new PageButton(1, this));
        menuContent.put(44, Button.getPlaceholder());

        return menuContent;
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

            return ItemMaker.of(perk.getMaterial()).setData(data.shortValue()).setDisplayName(perk.getDisplayName()).setLore(getLore()).build();
        }

        public List<String> getLore() {
            final List<String> toReturn = new ArrayList<>();
            for (String string : perk.getLore()) {
                toReturn.add(string);
            }
            toReturn.add("");
            toReturn.add(CC.translate("&6Left-Click &7to edit this rank perk."));
            return toReturn;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new RankPerkEditSubMenu(rank, perk).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class NewPerkButton extends Button {
        private final Rank rank;

        public String getMaterial() {
            return XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("menu.perk-editor.new-perk.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getInt("menu.perk-editor.new-perk.data");

        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("menu.perk-editor.new-perk.display-name"));
            for (String line : XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getStringList("menu.perk-editor.new-perk.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withFirstPrompt(new SetupPerkPrompt(this.rank)).withLocalEcho(false);
            player.beginConversation(factory.buildConversation(player));
        }
    }

    @AllArgsConstructor
    public static class SetupPerkPrompt extends StringPrompt {
        Rank rank;

        public String getPromptText(ConversationContext conversationContext) {
            return ChatColor.GOLD + "[Xenon] " + ChatColor.YELLOW + "Please hold the item stack you wish to use for this perk and type '" + ChatColor.WHITE + "confirm" + ChatColor.YELLOW + "'. Type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        public Prompt acceptInput(final ConversationContext conversationContext, final String input) {
            final Player sender = (Player)conversationContext.getForWhom();
            if (!input.equalsIgnoreCase("confirm")) {
                new RankPerkEditMenu(rank).openMenu(sender.getPlayer());
                conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &cYou have cancelled the process of editing the perk's material."));
                return SetupPerkPrompt.END_OF_CONVERSATION;
            }
            ItemStack itemStack = sender.getItemInHand().clone();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &cYou must be holding a valid item."));
                new RankPerkEditMenu(rank).openMenu(sender.getPlayer());
                return SetupPerkPrompt.END_OF_CONVERSATION;
            }
            if (itemStack.getItemMeta().getLore() == null) {
                ItemMeta im = itemStack.getItemMeta();
                im.setLore(Arrays.asList(CC.translate("&7Empty lore.")));
                itemStack.setItemMeta(im);
            }
            if (itemStack.getItemMeta().getDisplayName() == null) {
                ItemMeta im = itemStack.getItemMeta();
                im.setDisplayName(CC.translate("&cPerk #" + rank.getPerks().size()));
                itemStack.setItemMeta(im);
            }

            Bukkit.getLogger().severe("Saving for rank: " + rank.getName());
            rank.getPerks().add(new Perk("Perk" + rank.getPerks().size(), itemStack.getType().name(), itemStack.getType().getId(), itemStack.getItemMeta().getDisplayName(), itemStack.getItemMeta().getLore(), Arrays.asList("&7You can purchase this from our store!", "&6&ostore.ascendproyect.org")));
            rank.save();

            new RankPerkEditMenu(rank).openMenu(sender.getPlayer());
            return Prompt.END_OF_CONVERSATION;
        }
    }
}

