package dev.hely.core.bukkit.profile.notifications.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.crates.crates.menus.editor.settings.cratesettings.holograms
//   Date: Saturday, June 18, 2022 - 9:31 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.notifications.Notification;
import dev.hely.core.bukkit.profile.notifications.menu.NotificationListMenu;
import dev.hely.lib.callback.Callback;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import dev.hely.lib.sound.SoundManager;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class MessageEditorMenu extends PaginatedMenu {
    private final Notification notification;

    @Override
    public String getName() {
        return "Message Editor";
    }

    @Override
    public String getTitle(Player player) {
        return "Message Editor";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (String text_line : notification.getMessage()) {
            buttons.put(buttons.size(), new MessageLineButton(notification, text_line, buttons.size() + 1));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new BackButton(new NotificationListMenu()));
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
        menuContent.put(22, new NewLineButton(notification));
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());
        menuContent.put(26, new PageButton(1, this));


        return menuContent;
    }


    @AllArgsConstructor
    private static class MessageLineButton extends Button {
        private final Notification notification;
        private final String line_text;
        private final int position;

        @Override
        public ItemStack getItemStack(Player player) {
            Material material = Material.PAPER;
            int data = 0;
            String display_name = "&6#" + position + " Notification Line";

            return ItemMaker.of(material).setData((short) data).setDisplayName(display_name).setLore(getLore()).build();
        }

        public List<String> getLore() {
            List<String> lore = new ArrayList<>();

            lore.add("");
            lore.add("&7This message will appear #" + position + " in");
            lore.add("&7your notification!");
            lore.add("");
            lore.add("&6Line Text&7: &b'" + line_text + "'");
            lore.add("");
            lore.add("&6Right-Click &7this item to remove this message line.");

            return lore;
        }
        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<String> current_lines = new ArrayList<>();
            current_lines.addAll(notification.getMessage());

            if (notification.getMessage().size() == 0) {
                player.sendMessage(CC.translate("&cTheir has to be at least 1 notification line."));
                SoundManager.INSTANCE.playSound(player, "BLAZE_HIT");
                return;
            }

            current_lines.remove(line_text);

            notification.setMessage(current_lines);

            notification.setLast_update(System.currentTimeMillis());
            player.sendMessage(CC.translate("&cYou have successfully deleted this notification line!"));
            SoundManager.INSTANCE.playSound(player, "BLAZE_HIT");

            notification.save();
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }
    @AllArgsConstructor
    private static class NewLineButton extends Button {
        private final Notification notification;

        @Override
        public ItemStack getItemStack(Player player) {
            Material material = Material.NAME_TAG;
            int data = 0;
            String display_name = "&6New Notification Line";

            return ItemMaker.of(material).setData((short) data).setDisplayName(display_name).setLore(getLore()).build();
        }

        public List<String> getLore() {
            List<String> lore = new ArrayList<>();

            lore.add("");
            lore.add("&7Click here to create a new message");
            lore.add("&7text line that will appear in");
            lore.add("&7the notification!");

            return lore;
        }
        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<String> newMessage;
            newMessage = notification.getMessage();

            player.closeInventory();
            player.sendMessage(CC.translate("&cPlease type the new text line for the notification!"));
            player.sendMessage(CC.translate("&cPlaceholders: &b%player%"));
            player.sendMessage(CC.translate("&7"));
            player.sendMessage(CC.translate("&cYou can type '&4cancel&c' to cancel this."));

            conversationString(player, (text_line) -> {
                player.sendMessage(CC.translate(""));
                player.sendMessage(CC.translate("&aYou have successfully added a new text line to this notification."));
                player.sendMessage(CC.translate("&aNew Notification Line: &b" + text_line));
                player.sendMessage(CC.translate(""));
                newMessage.add(text_line);
                notification.setLast_update(System.currentTimeMillis());
                notification.setMessage(newMessage);
                notification.save();
            });
        }


        private void conversationString(Player p, Callback<String> callback) {
            ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

                public String getPromptText(ConversationContext context) {
                    return "";
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String text) {
                    notification.getMessage().add(text);
                    notification.save();
                    new MessageEditorMenu(notification).openMenu(p);

                    return Prompt.END_OF_CONVERSATION;
                }

            }).withLocalEcho(false).withEscapeSequence("cancel").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");

            Conversation con = factory.buildConversation(p);
            p.beginConversation(con);
        }
    }
}
