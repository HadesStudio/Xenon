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
//   Date: Monday, January 22, 2024 - 10:04 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//   Ascend LLC Discord: https://discord.gg/xM8bktgC6q
//============================================================

import com.cryptomorin.xseries.XSound;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.perks.impl.RankPerkEditSubMenu;
import net.ascend.core.bukkit.rank.rank.perk.Perk;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.CC;
import net.ascend.lib.callback.Callback;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PerkEditorLoreButton extends Button {
    Perk perk;
    Rank rank;
    private static int index;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of(MaterialUtil.ENDER_PORTAL_FRAME.getMaterial()).setDisplayName("&6Edit Lore").setLore(getLore()).build();
    }

    public List<String> getLore() {
        final List<String> toReturn = new ArrayList<>();
        toReturn.add("");
        int i = 0;
        for (final String s : perk.getLore()) {
            toReturn.add(CC.translate(((index == i) ? "&a" : "&7") + i + ". &f" + CC.translate(s)));
            ++i;
        }
        toReturn.add("");
        toReturn.add(CC.translate("&6Shift + Left-Click &7to add a lore line."));
        toReturn.add(CC.translate("&6Shift + Right-Click &7to delete this lore line."));
        toReturn.add(CC.translate("&6Left / Right-Click &7to scroll lore lines."));
        toReturn.add(CC.translate("&6Middle-Click &7to edit this lore line."));
        return toReturn;
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        String currentLine = perk.getLore().isEmpty() ? "" : perk.getLore().get(index);
        if (clickType.equals(ClickType.MIDDLE)) {
            player.closeInventory();
            TextChangePrompt stringPrompt = new TextChangePrompt(perk, rank, (callback -> {
                List<String> lore = perk.getLore();
                lore.remove(currentLine);
                lore.add(index, String.valueOf(callback));

                perk.setLore(lore); // Update locally I think?
                rank.getPerks().remove(perk);
                rank.getPerks().add(new Perk(perk.getName(), perk.getMaterial(), perk.getMaterialData(), perk.getDisplayName(), lore, perk.getClickMessage()));
                rank.save();
            }));
            ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withFirstPrompt(stringPrompt).withLocalEcho(false);
            player.beginConversation(factory.buildConversation(player));
            return;
        }
        if (!clickType.isShiftClick()) {
            XSound.play(player.getLocation(), "WOOD_CLICK");
            ++index;
            if (index >= perk.getLore().size()) {
                index = 0;
            }
            return;
        }
        if (clickType.isLeftClick()) {
            player.closeInventory();
            final TextChangePrompt stringPrompt = new TextChangePrompt(perk, rank, (callback -> {
                List<String> lore = perk.getLore();
                lore.add(callback);

                perk.setLore(lore); // Update locally I think?
                rank.getPerks().remove(perk);
                rank.getPerks().add(new Perk(perk.getName(), perk.getMaterial(), perk.getMaterialData(), perk.getDisplayName(), lore, perk.getClickMessage()));
                rank.save();
            }));
            final ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withFirstPrompt(stringPrompt).withLocalEcho(false);
            player.beginConversation(factory.buildConversation(player));
            return;
        }
        if (clickType.isRightClick()) {
            List<String> lore = perk.getLore();
            lore.remove(currentLine);

            perk.setLore(lore); // Update locally I think?
            rank.getPerks().remove(perk);
            rank.getPerks().add(new Perk(perk.getName(), perk.getMaterial(), perk.getMaterialData(), perk.getDisplayName(), lore, perk.getClickMessage()));
            rank.save();

            --index;
            if (index >= perk.getLore().size()) {
                index = 0;
            }
        }
    }

    @Override
    public boolean toUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }

    @AllArgsConstructor
    public static class TextChangePrompt extends StringPrompt {
        Perk perk;
        Rank rank;
        Callback<String> callback;

        public String getPromptText(ConversationContext conversationContext) {
            return ChatColor.GOLD + "[Xenon] " + ChatColor.YELLOW + "Please type the text for the new lore line for this rank perk. Type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        public Prompt acceptInput(final ConversationContext conversationContext, final String input) {
            final Player sender = (Player)conversationContext.getForWhom();
            if (input.equalsIgnoreCase("cancel")) {
                new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
                conversationContext.getForWhom().sendRawMessage(CC.translate("&6[Xenon] &cYou have cancelled editing the perk lore lines."));
                return TextChangePrompt.END_OF_CONVERSATION;
            }
            callback.callback(input);
            new RankPerkEditSubMenu(rank, perk).openMenu(sender.getPlayer());
            return Prompt.END_OF_CONVERSATION;
        }
    }
}
