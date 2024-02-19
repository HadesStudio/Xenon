package net.ascend.core.bukkit.profile.menu.submenus;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.menu.EditProfileMenu;
import net.ascend.core.bukkit.profile.note.Note;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
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
        return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.title").replace("%player%", profile.getName());
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

        public String getMaterial() {
            return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.note-button.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("notes-menu.buttons.note-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.note-button.display-name"));
            for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("notes-menu.buttons.note-button.lore")) {
                builder.addLore(line
                        .replace("%message%", note.getAddedReason())
                        .replace("%staff_member%", note.getAddedBy())
                        .replace("%creation_date%", TimeUtil.formatDateDiff(note.getAddedAt()))
                        .replace("%creation_server%", note.getAddedServer())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("xenon.staff.notes.delete")) {
                    profile.getNotes().remove(note);
                    profile.save();
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.removed-note").replace("%player%", profile.getName())));
                } else {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.cannot-delete")));
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

        public String getMaterial() {
            return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.create-button.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("notes-menu.buttons.create-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.buttons.create-button.display-name"));
            for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("notes-menu.buttons.create-button.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("xenon.staff.notes.create")) {
                    player.closeInventory();
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.create-promt")));
                    ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
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
                            note.setAddedServer(XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));

                            profile.getNotes().add(note);
                            profile.save();


                            player.sendMessage(CC.translate(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.create-note").replace("%player%", profile.getName())));
                            new NotesMenu(profile).openMenu(player);
                            return Prompt.END_OF_CONVERSATION;
                        }
                    }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

                    Conversation con = factory.buildConversation(player);
                    player.beginConversation(con);
                } else {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("notes-menu.messages.cannot-create")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }
}
