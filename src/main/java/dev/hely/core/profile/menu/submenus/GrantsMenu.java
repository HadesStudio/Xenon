package dev.hely.core.profile.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.menu.submenus
//   Date: Tuesday, August 01, 2023 - 4:07 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.menu.EditProfileMenu;
import dev.hely.core.profile.note.Note;
import dev.hely.core.ranks.grants.Grant;
import dev.hely.core.utils.DateUtils;
import dev.hely.core.utils.DurationWrapper;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class GrantsMenu extends PaginatedMenu {
    final Profile profile;

    @Override
    public String getName() {
        return Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Grant grants : profile.getGrants()) {
            buttons.put(buttons.size(), new Grants_Button(profile, grants));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new BackButton(new EditProfileMenu(profile)));
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());
        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());
        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());
        menuContent.put(9, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());

        menuContent.put(18, new PageButton(-1, this));
        menuContent.put(19, Button.getPlaceholder());
        menuContent.put(20, Button.getPlaceholder());
        menuContent.put(21, Button.getPlaceholder());
        menuContent.put(22, new Grant_Add_Button(profile));
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());

        menuContent.put(26, new PageButton(1, this));


        return menuContent;
    }

    @AllArgsConstructor
    public static class Grants_Button extends Button {
        final Profile profile;
        final Grant grant;

        public Material getMaterial() {
            if (Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.grants-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.grants-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.grants-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grants-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'grants-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("grants-menu.buttons.grants-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.grants-button.display-name").replace("%rank_display%", grant.getRank().getName()));
            for (String line : Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("grants-menu.buttons.grants-button.lore")) {
                builder.addLore(line
                        .replace("%rank_name%", grant.getRankName())
                        .replace("%rank_duration%", DateUtils.formatDateDiff(grant.getDuration()))
                        .replace("%rank_expire%", grant.getRemaining())
                        .replace("%reason%", grant.getReason())
                        .replace("%staff_member%", grant.getAdder())
                        .replace("%added_date%", DateUtils.formatDateDiff(grant.getAddedTime()))
                        .replace("%removed_date%", DateUtils.formatDateDiff(grant.getRemovedTime()))
                        .replace("%creation_server%", grant.getServer())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("oxygen.staff.grants.remove")) {
                    profile.getGrants().remove(grant);
                    profile.save();
                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.removed-grant").replace("%player%", profile.getName())));
                } else {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.cannot-delete")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Grant_Add_Button extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.create-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.create-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.create-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'create-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'create-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("grants-menu.buttons.create-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.create-button.display-name"));
            for (String line : Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("grants-menu.buttons.create-button.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("oxygen.staff.grants.add")) {
                    player.closeInventory();
                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.promt-1")));
                    ConversationFactory factory = new ConversationFactory(Oxygen.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                        public String getPromptText(ConversationContext context) {
                            return "";
                        }

                        @Override
                        public Prompt acceptInput(ConversationContext cc, String rankName) {
                            if (Oxygen.getInstance().getRankManager().getRank(rankName) == null) {
                                cc.getForWhom().sendRawMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.invalid-rank")));
                                return Prompt.END_OF_CONVERSATION;
                            }

                            cc.getForWhom().sendRawMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.promt-2")));
                            ConversationFactory factory = new ConversationFactory(Oxygen.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                                public String getPromptText(ConversationContext context) {
                                    return "";
                                }

                                @Override
                                public Prompt acceptInput(ConversationContext cc, String reason) {
                                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.promt-3")));
                                    ConversationFactory factory = new ConversationFactory(Oxygen.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                                        public String getPromptText(ConversationContext context) {
                                            return "";
                                        }

                                        @Override
                                        public Prompt acceptInput(ConversationContext cc, String duration) {
                                            if (duration.equalsIgnoreCase("permanent")) {
                                                Grant grant = new Grant();
                                                grant.setActive(true);
                                                grant.setAdder(player.getName());
                                                grant.setPermanent(true);
                                                grant.setRankName(rankName);
                                                grant.setServer(Oxygen.getInstance().getServer().getServerName());
                                                grant.setReason(reason);

                                                new GrantsMenu(profile).openMenu(player);
                                            } else {
                                                final DurationWrapper durationWrapper = DurationWrapper.findDurationWrapper(duration);
                                                Grant grant = new Grant();
                                                grant.setActive(true);
                                                grant.setAdder(player.getName());
                                                grant.setPermanent(false);
                                                grant.setDuration(durationWrapper.getDuration());
                                                grant.setRankName(rankName);
                                                grant.setServer(Oxygen.getInstance().getServer().getServerName());
                                                grant.setReason(reason);

                                                new GrantsMenu(profile).openMenu(player);
                                            }
                                            return Prompt.END_OF_CONVERSATION;
                                        }
                                    }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

                                    Conversation con = factory.buildConversation(player);
                                    player.beginConversation(con);
                                    return Prompt.END_OF_CONVERSATION;
                                }
                            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

                            Conversation con = factory.buildConversation(player);
                            player.beginConversation(con);
                            return Prompt.END_OF_CONVERSATION;
                        }
                    }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

                    Conversation con = factory.buildConversation(player);
                    player.beginConversation(con);
                } else {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.cannot-add")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

}
