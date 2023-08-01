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
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.menu.EditProfileMenu;
import dev.hely.core.profile.siblings.Sibling;
import dev.hely.core.utils.DateUtils;
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
public class SiblingsMenu extends PaginatedMenu {
    final Profile profile;

    @Override
    public String getName() {
        return Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Sibling siblings : profile.getSiblings()) {
            buttons.put(buttons.size(), new Sibling_Button(profile, siblings));
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
        menuContent.put(22, new Sibling_Create_Button(profile));
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());

        menuContent.put(26, new PageButton(1, this));


        return menuContent;
    }

    @AllArgsConstructor
    public static class Sibling_Button extends Button {
        final Profile profile;
        final Sibling sibling;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.SKULL_ITEM);
            builder.setData((short) 3);
            builder.setOwner(sibling.getSiblingName());
            builder.setDisplayName(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.buttons.sibling-button.display-name").replace("%player_name%", sibling.getSiblingName()));
            for (String line : Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("siblings-menu.buttons.sibling-button.lore")) {
                builder.addLore(line
                        .replace("%staff_member%", sibling.getAddedBy())
                        .replace("%sibling_name%", sibling.getSiblingName())
                        .replace("%creation_date%", DateUtils.formatDateDiff(sibling.getAddedAt()))
                        .replace("%creation_server%", sibling.getAddedServer())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("oxygen.staff.sibling.delete")) {
                    Profile siblingsProfile = Oxygen.getInstance().getProfileManager().getProfile(sibling.getMainProfileName());
                    siblingsProfile.getSiblings().remove(sibling);
                    profile.getSiblings().remove(sibling);
                    profile.save();
                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.messages.removed-sibling").replace("%player%", profile.getName()).replace("%sibling%", sibling.getSiblingName())));
                } else {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.messages.cannot-delete")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Sibling_Create_Button extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.buttons.create-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.buttons.create-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.buttons.create-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'siblings-create-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'siblings-create-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("siblings-menu.buttons.create-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.buttons.create-button.display-name"));
            for (String line : Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("siblings-menu.buttons.create-button.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("oxygen.staff.sibling.create")) {
                    player.closeInventory();
                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.messages.create-promt")));
                    ConversationFactory factory = new ConversationFactory(Oxygen.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                        public String getPromptText(ConversationContext context) {
                            return "";
                        }

                        @Override
                        public Prompt acceptInput(ConversationContext cc, String s) {
                            if (Oxygen.getInstance().getProfileManager().getProfile(s) == null) {
                                cc.getForWhom().sendRawMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", s));
                                return END_OF_CONVERSATION;
                            }
                            Profile specifiedProfile = Oxygen.getInstance().getProfileManager().getProfile(s);
                            for (Sibling siblings : profile.getSiblings()) {
                                if (siblings.getSiblingName().equalsIgnoreCase(specifiedProfile.getName())) {
                                    cc.getForWhom().sendRawMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.messages.already-added")));
                                    return END_OF_CONVERSATION;
                                }
                            }
                            Sibling profileSibling = new Sibling();
                            profileSibling.setMainProfileName(profile.getName());
                            profileSibling.setMainProfileUUID(profile.getUniqueId());
                            profileSibling.setSiblingName(specifiedProfile.getName());
                            profileSibling.setSiblingUUID(specifiedProfile.getUniqueId());
                            profileSibling.setUniqueId(UUID.randomUUID());
                            profileSibling.setAddedAt(System.currentTimeMillis());
                            profileSibling.setAddedBy(player.getName());
                            profileSibling.setAddedServer(Oxygen.getInstance().getServerManager().getServerName());
                            profile.getSiblings().add(profileSibling);
                            profile.save();
                            specifiedProfile.getSiblings().add(profileSibling);
                            specifiedProfile.save();

                            player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.messages.create-sibling").replace("%player%", profile.getName())));
                            new SiblingsMenu(profile).openMenu(player);
                            return Prompt.END_OF_CONVERSATION;
                        }
                    }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

                    Conversation con = factory.buildConversation(player);
                    player.beginConversation(con);
                } else {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getEditProfileMenuConfig().getConfiguration().getString("siblings-menu.messages.cannot-create")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

}
