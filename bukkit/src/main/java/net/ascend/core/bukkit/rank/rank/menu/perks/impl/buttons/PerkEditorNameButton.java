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

import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.perks.impl.RankPerkEditSubMenu;
import net.ascend.core.bukkit.rank.rank.perk.Perk;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class PerkEditorNameButton extends Button {
    Perk perk;
    Rank rank;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of("SIGN").setDisplayName("&6Edit Name").setLore(Arrays.asList("&7This is the identifier of the", "&7perk.", "&6Current Name&7: &f" + perk.getName(), "", "&6Left-Click &7to edit the identifier.")).build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withFirstPrompt(new EditIdentifier(perk, rank)).withLocalEcho(false);
        player.beginConversation(factory.buildConversation(player));
    }

    @AllArgsConstructor
    public static class EditIdentifier extends StringPrompt {
        Perk perk;
        Rank rank;

        public String getPromptText(ConversationContext conversationContext) {
            return ChatColor.GOLD + "[Xenon] " + ChatColor.YELLOW + "Please type the new identifier for this rank perk. Type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        public Prompt acceptInput(final ConversationContext conversationContext, final String input) {
            final Player sender = (Player)conversationContext.getForWhom();
            if (input.equalsIgnoreCase("cancel")) {
                new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
                conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &cYou have cancelled editing the perk identifier."));
                return EditIdentifier.END_OF_CONVERSATION;
            }
            conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &aYou have successfully updated the perk identifier of " + perk.getName() + " &ato &6" + input + "&a."));

            rank.getPerks().remove(perk);
            rank.getPerks().add(new Perk(input, perk.getMaterial(), perk.getMaterialData(), perk.getDisplayName(), perk.getLore(), perk.getClickMessage()));
            rank.save();

            new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
            return Prompt.END_OF_CONVERSATION;
        }
    }
}
