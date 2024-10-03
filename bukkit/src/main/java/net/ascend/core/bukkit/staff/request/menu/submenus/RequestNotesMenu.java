package net.ascend.core.bukkit.staff.request.menu.submenus;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.staff.request.Request;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class RequestNotesMenu extends PaginatedMenu {

    private final Request request;

    @Override
    public String getName() {
        return "Request Notes";
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public String getTitle(Player player) {
        return "Request Notes";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (String notes : request.getStaffNotes()) {
            buttons.put(buttons.size(), new Notes_Button(notes, request));
        }

        return buttons;
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new BackButton(new ActiveRequestsMenu()));
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());

        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());


        menuContent.put(27, new PageButton(-1, this));
        menuContent.put(28, Button.getPlaceholder());
        menuContent.put(29, Button.getPlaceholder());

        menuContent.put(30, Button.getPlaceholder());
        menuContent.put(31, new Note_Create_Button(request));
        menuContent.put(32, Button.getPlaceholder());

        menuContent.put(33, Button.getPlaceholder());
        menuContent.put(34, Button.getPlaceholder());
        menuContent.put(35, new PageButton(1, this));

        return menuContent;
    }

    @AllArgsConstructor
    public static class Notes_Button extends Button {
        private final String message;
        private final Request request;

        public String getMaterial() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests-menu.buttons.notes.material");
        }

        public List<String> getLore() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests-menu.buttons.notes.lore");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("active-requests-menu.buttons.notes.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests-menu.buttons.notes.display_name"));

            for (String line : getLore()) {
                builder.addLore(line
                        .replace("%message%", message)
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                request.getStaffNotes().remove(message);
                request.save();
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isLeftClick();
        }
    }

    @AllArgsConstructor
    public static class Note_Create_Button extends Button {

        private final Request request;

        public String getMaterial() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests-menu.buttons.new_note.material");
        }

        public List<String> getLore() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests-menu.buttons.new_note.lore");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("active-requests-menu.buttons.new_note.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests-menu.buttons.new_note.display_name"));

            for (String line : getLore()) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            player.sendMessage(CC.translate(Locale.REQUEST_NOTE_PROMT));

            ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return "";
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    request.getStaffNotes().add(s);
                    request.save();

                    player.sendMessage(CC.translate(Locale.REQUEST_NOTE_ADDED));

                    new RequestNotesMenu(request).openMenu(player);
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

            Conversation con = factory.buildConversation(player);
            player.beginConversation(con);
        }
    }
}
