package dev.hely.core.bukkit.profile.menu.submenus;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.note.Note;
import dev.hely.core.bukkit.profile.menu.EditProfileMenu;
import dev.hely.core.bukkit.utils.DateUtils;
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
public class NotesMenu extends PaginatedMenu {
    final Profile profile;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Note notes : profile.getNotes()) {
            buttons.put(buttons.size(), new Note_Button(profile, notes));
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
        menuContent.put(22, new Note_Create_Button(profile));
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());

        menuContent.put(26, new PageButton(1, this));


        return menuContent;
    }

    @AllArgsConstructor
    public static class Note_Button extends Button {
        final Profile profile;
        final Note note;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.note-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.note-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.note-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'note-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'note-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("notes-menu.buttons.note-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.note-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("notes-menu.buttons.note-button.lore")) {
                builder.addLore(line
                        .replace("%message%", note.getAddedReason())
                        .replace("%staff_member%", note.getAddedBy())
                        .replace("%creation_date%", DateUtils.formatDateDiff(note.getAddedAt()))
                        .replace("%creation_server%", note.getAddedServer())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("oxygen.staff.notes.delete")) {
                    profile.getNotes().remove(note);
                    profile.save();
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.removed-note").replace("%player%", profile.getName())));
                } else {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.cannot-delete")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Note_Create_Button extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.create-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.create-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.create-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'create-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'create-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("notes-menu.buttons.create-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.create-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("notes-menu.buttons.create-button.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("oxygen.staff.notes.create")) {
                    player.closeInventory();
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.create-promt")));
                    ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                        public String getPromptText(ConversationContext context) {
                            return "";
                        }

                        @Override
                        public Prompt acceptInput(ConversationContext cc, String s) {
                            Note note = new Note();
                            note.setUniqueId(UUID.randomUUID());
                            note.setAddedAt(System.currentTimeMillis());
                            note.setAddedBy(player.getName());
                            note.setAddedReason(s);
                            note.setAddedServer(OxygenBukkit.getInstance().getServerManager().getServerName());

                            profile.getNotes().add(note);
                            profile.save();


                            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.create-note").replace("%player%", profile.getName())));
                            new NotesMenu(profile).openMenu(player);
                            return Prompt.END_OF_CONVERSATION;
                        }
                    }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

                    Conversation con = factory.buildConversation(player);
                    player.beginConversation(con);
                } else {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.cannot-create")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }
}
