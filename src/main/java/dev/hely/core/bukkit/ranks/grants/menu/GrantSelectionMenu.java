package dev.hely.core.bukkit.ranks.grants.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.ranks.grants.menu
//   Date: Friday, September 15, 2023 - 8:20 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.events.GrantGivenEvent;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.material.ColorUtil;
import dev.hely.lib.util.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class GrantSelectionMenu extends PaginatedMenu {
    Profile profile;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grant-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grant-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        final Comparator<Rank> GRANT_COMPARATOR = Comparator.comparingInt(Rank::getWeight).reversed(); // Sort the grant's by weight :)
        Map<Integer, Button> buttons = new HashMap<>();

        OxygenBukkit.getInstance().getRankManager().getRanks().stream().sorted(GRANT_COMPARATOR).forEach(grant -> buttons.put(buttons.size(), new Rank_Button(profile, grant)));

        return buttons;
    }

    @AllArgsConstructor
    public static class Rank_Button extends Button {
        private final Profile profile;
        private final Rank rank;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grant-menu.buttons.rank-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grant-menu.buttons.rank-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grant-menu.buttons.rank-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grant-menu-rank-button' is null, using a default material."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grant-menu-rank-button' is broken, using a default material."));
                return Material.WOOL;
            }
        }

        public short getData() {
            if (OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getBoolean("grant-menu.buttons.rank-button.data.match-display-color")) {
                return ColorUtil.COLOR_MAP.get(rank.getDisplay_color()).getWoolData();
            } else {
                return (short) OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("grant-menu.buttons.rank-button.data.data");
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(rank.getDisplay_color() + OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grant-menu.buttons.rank-button.title").replace("%rank_name%", rank.getName()));
            for (String line : OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("grant-menu.buttons.rank-button.lore")) {
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
            if (clickType == ClickType.LEFT) {
                player.closeInventory();
                ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withFirstPrompt(new durationPromt(profile, rank)).withLocalEcho(false);

                player.beginConversation(factory.buildConversation(player));
            }
        }
    }


    @AllArgsConstructor
    public static class durationPromt extends StringPrompt {
        private Profile profile;
        Rank rank;

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grant-menu.messages.grant-duration"));
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String input) {
            Player sender = (Player) conversationContext.getForWhom();

            if (input.equalsIgnoreCase("cancel")) {
                sender.playSound(sender.getLocation(), Sound.BLAZE_HIT, 5, 5);
                conversationContext.getForWhom().sendRawMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.canceled-promt")));
                new GrantSelectionMenu(profile).openMenu(sender);
                return END_OF_CONVERSATION;
            }

            long duration = TimeUtil.parseTime(input);
            boolean permanent = input.equalsIgnoreCase("perm") || input.equalsIgnoreCase("permanent"); // By default, we set it to false, probably, their is a way better way of doing this, but hey, I will do it like this. :)
            if (duration == -1 && !input.equalsIgnoreCase("perm") || !input.equalsIgnoreCase("permanent")) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-time")));
                new GrantSelectionMenu(profile).openMenu(sender);
                return END_OF_CONVERSATION;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    Grant grant = new Grant();
                    grant.setActive(true);
                    grant.setAdder(sender.getName());
                    grant.setDuration(duration);
                    grant.setPermanent(permanent);
                    grant.setRankName(rank.getName());
                    grant.setAddedTime(System.currentTimeMillis());
                    grant.setReason("Given via /grant");
                    new GrantGivenEvent(grant, profile, sender.getPlayer());
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.given").replace("%player%", profile.getName()).replace("%rank%", rank.getName()).replace("%time%", String.valueOf(duration))));
                    sender.playSound(sender.getLocation(), Sound.ORB_PICKUP, 5, 5);
                }
            }.runTask(OxygenBukkit.getInstance());

            return Prompt.END_OF_CONVERSATION;
        }
    }

    public void promptTime(Player player, Profile profile, Rank rank) {
        final ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withFirstPrompt(new durationPromt(profile, rank)).withLocalEcho(false);

        player.beginConversation(factory.buildConversation(player));
    }
}
