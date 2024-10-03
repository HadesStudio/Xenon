package net.ascend.core.bukkit.profile.edit.submenus;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.edit.EditProfileMenu;
import net.ascend.core.bukkit.profile.note.Note;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class NotesMenu extends PaginatedMenu {

    final Profile profile;
    final boolean backButton;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notes-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notes-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();


        List<Note> notes = profile.getNotes();
        notes.sort(Comparator.comparingLong(Note::getAddedAt));

        int id = 1;

        for (Note note : notes) {
            buttons.put(buttons.size(), new NoteButton(profile, note, id++));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        if (backButton) {
            menuContent.put(0, new BackButton(new EditProfileMenu(profile)));
        } else {
            menuContent.put(0, Button.getPlaceholder());
        }

        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());
        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());
        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(18, new PageButton(-1, this));
        menuContent.put(19, Button.getPlaceholder());
        menuContent.put(20, Button.getPlaceholder());
        menuContent.put(21, Button.getPlaceholder());
        menuContent.put(22, new NoteCreateButton(profile));
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());

        menuContent.put(26, new PageButton(1, this));


        return menuContent;
    }

    @AllArgsConstructor
    public static class NoteButton extends Button {

        final Profile profile;
        final Note note;
        final int id;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notes-menu.buttons.note.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("notes-menu.buttons.note.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(
                    XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notes-menu.buttons.note.display_name")
                    .replace("%id%", String.valueOf(id)));

            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("notes-menu.buttons.note.lore")) {
                builder.addLore(line
                        .replace("%id%", String.valueOf(id))
                        .replace("%message%", note.getAddedReason())
                        .replace("%staff_member%", note.getAddedBy())
                        .replace("%creation_date%", TimeUtil.formatIntoCalendarString(new Date(note.getAddedAt())))
                        .replace("%creation_server%", note.getAddedServer())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                profile.getNotes().remove(note);
                profile.save();

                player.sendMessage(CC.translate(Locale.NOTES_REMOVE_REMOVED)
                        .replace("%id%", String.valueOf(id))
                        .replace("%player%", profile.getName()));
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isLeftClick();
        }
    }

    @AllArgsConstructor
    public static class NoteCreateButton extends Button {

        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notes-menu.buttons.create.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("notes-menu.buttons.create.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notes-menu.buttons.create.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("notes-menu.buttons.create.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                player.closeInventory();
                player.sendMessage(CC.translate("&aPlease type the message for the &lNote &ainto the chat."));
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
                        note.setAddedServer(XenonBukkit.getInstance().getServerManager().getCurrentServerName());

                        profile.getNotes().add(note);
                        profile.save();

                        player.sendMessage(CC.translate(Locale.NOTES_ADD_ADDED)
                                .replace("%id%", String.valueOf(profile.getNotes().size()))
                                .replace("%player%", profile.getName()));

                        new NotesMenu(profile, true).openMenu(player);
                        return Prompt.END_OF_CONVERSATION;
                    }
                }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

                Conversation con = factory.buildConversation(player);
                player.beginConversation(con);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }
}
