package net.ascend.core.bukkit.rank.rank.menu.perks.impl.buttons;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
// Ascend LLC Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: net.ascend.core.bukkit.rank.rank.menu.perks.impl.buttons
//   Date: Monday, January 22, 2024 - 9:17 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//   Ascend LLC Discord: https://discord.gg/xM8bktgC6q
//============================================================

import com.google.common.primitives.Ints;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.perks.RankPerkEditMenu;
import net.ascend.core.bukkit.rank.rank.menu.perks.impl.RankPerkEditSubMenu;
import net.ascend.core.bukkit.rank.rank.perk.Perk;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class PerkEditorItemStackButton extends Button {
    Perk perk;
    Rank rank;

    @Override
    public ItemStack getItemStack(Player player) {
        List<String> lore = Arrays.asList(
                "&7Wanna change the material display",
                "&7of this perk in the menu?",
                "&7",
                "&6Current Material&7: &f" + perk.getMaterial().toString() + " (" + perk.getMaterialData() + ")",
                "&7",
                "&6Left-Click &7to change the material.",
                "&6Right-Click &7to change material data."
        );
        Integer data = perk.getMaterialData();

        return ItemMaker.of(perk.getMaterial()).setData(data.shortValue()).setLore(lore).build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (clickType.isLeftClick()) {
            player.closeInventory();
            ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withFirstPrompt(new EditPerkMaterial(rank, perk)).withLocalEcho(false);
            player.beginConversation(factory.buildConversation(player));
        } else {

        }
    }

    @AllArgsConstructor
    public static class EditPerkMaterial extends StringPrompt {
        Rank rank;
        Perk perk;

        public String getPromptText(ConversationContext conversationContext) {
            return ChatColor.GOLD + "[Xenon] " + ChatColor.YELLOW + "Please hold the new material you wish to use for this perk and type '" + ChatColor.WHITE + "confirm" + ChatColor.YELLOW + "'. Type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        public Prompt acceptInput(final ConversationContext conversationContext, final String input) {
            final Player sender = (Player)conversationContext.getForWhom();
            if (!input.equalsIgnoreCase("confirm")) {
                new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
                conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &cYou have cancelled the process of editing the perk's material."));
                return EditPerkMaterial.END_OF_CONVERSATION;
            }

            ItemStack itemStack = sender.getItemInHand().clone();
            Material materialInHand = sender.getItemInHand().getType();

            List<String> lore = new ArrayList<>();
            String displayName = "";

            if (itemStack == null || itemStack.getType() == Material.AIR) {
                conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &cYou must be holding a valid item."));
                new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
                return RankPerkEditMenu.SetupPerkPrompt.END_OF_CONVERSATION;
            }
            if (itemStack.getItemMeta().getLore() == null) { // Null check to remove error xd, this allows them to put pre-built items in the menu.
                lore = perk.getLore();
            }
            if (itemStack.getItemMeta().getDisplayName() == null) { // Null check to remove error xd, this allows them to put pre-built items in the menu.
                displayName = perk.getDisplayName();
            }

            rank.getPerks().remove(perk);
            rank.getPerks().add(new Perk(perk.getName(), materialInHand.name(), perk.getMaterialData(), displayName, lore, perk.getClickMessage()));
            rank.save();

            new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
            return Prompt.END_OF_CONVERSATION;
        }
    }

    @AllArgsConstructor
    public static class EditMaterialDataPrompt extends StringPrompt {
        Rank rank;
        Perk perk;

        public String getPromptText(ConversationContext conversationContext) {
            return ChatColor.GOLD + "[Xenon] " + ChatColor.YELLOW + "Please hold the new material data you wish to use for this perk and type '" + ChatColor.WHITE + "confirm" + ChatColor.YELLOW + "'. Type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        public Prompt acceptInput(final ConversationContext conversationContext, final String input) {
            final Player sender = (Player)conversationContext.getForWhom();
            if (input.equalsIgnoreCase("cancel")) {
                new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
                conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &cYou have cancelled the process of editing the categories slot."));
                return EditMaterialDataPrompt.END_OF_CONVERSATION;
            }
            Integer number = Ints.tryParse(input);
            if (number == null) {
                conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &cYou must provide a valid number."));
                new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
                return EditMaterialDataPrompt.END_OF_CONVERSATION;
            }

            conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &aYou have updated the material data of this perk to &6" + input + "&a."));

            rank.getPerks().remove(perk);
            rank.getPerks().add(new Perk(perk.getName(), perk.getMaterial(), number, perk.getDisplayName(), perk.getLore(), perk.getClickMessage()));
            rank.save();

            return Prompt.END_OF_CONVERSATION;
        }
    }
}
