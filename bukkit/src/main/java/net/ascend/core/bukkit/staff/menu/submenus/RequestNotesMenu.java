package net.ascend.core.bukkit.staff.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.staff.menu.submenus
//   Date: Sunday, July 30, 2023 - 8:04 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.staff.requests.Request;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.PageButton;
import net.ascend.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class RequestNotesMenu extends PaginatedMenu {

    Request request;

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

        menuContent.put(0, Button.getPlaceholder());
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
        menuContent.put(18, Button.getPlaceholder());
        menuContent.put(26, Button.getPlaceholder());

        menuContent.put(27, Button.getPlaceholder());
        menuContent.put(28, new PageButton(-1, this));
        menuContent.put(29, Button.getPlaceholder());

        menuContent.put(30, Button.getPlaceholder());
        menuContent.put(31, new Note_Create_Button(request));
        menuContent.put(32, Button.getPlaceholder());

        menuContent.put(33, Button.getPlaceholder());
        menuContent.put(34, new PageButton(1, this));
        menuContent.put(35, Button.getPlaceholder());

        return menuContent;
    }

    @AllArgsConstructor
    public static class Notes_Button extends Button {
        String message;
        Request request;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.notes.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.notes.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.notes.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'active-notes-button' is null, using a default material."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'active-notes-button' is broken, using a default material."));
                return Material.PAPER;
            }
        }

        public List<String> getLore() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests.buttons.notes.lore");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("active-requests.buttons.notes.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.notes.display-name"));

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
                request.getStaffNotes().stream().filter(type -> type.equalsIgnoreCase(message));
            }
        }
    }

    @AllArgsConstructor
    public static class Note_Create_Button extends Button {
        Request request;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.new-note.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.new-note.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.new-note.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'new-notes-button' is null, using a default material."));
                    return Material.NAME_TAG;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'new-notes-button' is broken, using a default material."));
                return Material.NAME_TAG;
            }
        }

        public List<String> getLore() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests.buttons.new-note.lore");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("active-requests.buttons.new-note.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.new-note.display-name"));

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
