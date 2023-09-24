package dev.hely.core.bukkit.profile.notifications.menu.submenus;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.notifications.Notification;
import dev.hely.lib.callback.Callback;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.fill.FillerTemplate;
import dev.hely.lib.menu.implement.ConfirmationMenu;
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
public class SelectionMenu extends Menu {
    List<String> message;
    boolean create;

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return "Select Type";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle(Player player) {
        return "Select Type";
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new SendPlayerButton(message, create));
        buttons.put(14, new SendAllButton(message, create));

        return buttons;
    }

    @AllArgsConstructor
    private static class SendPlayerButton extends Button {
        private final List<String> newMessage;
        boolean create;

        @Override
        public ItemStack getItemStack(Player player) {
            Material material = Material.LAPIS_BLOCK;
            int data = 0;
            String display_name = "&6Send Notification to Player";

            return ItemMaker.of(material).setData((short) data).setDisplayName(display_name).setLore(getLore()).build();
        }

        public List<String> getLore() {
            List<String> lore = new ArrayList<>();

            lore.add("");
            lore.add("&7Click here to send this");
            lore.add("&7notification to a specific player.");

            return lore;
        }
        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            conversationString(player, (specified) -> {
                new ConfirmationMenu("Send notification?", (b) -> {
                    if (b) {
                        player.sendMessage(CC.translate(""));
                        player.sendMessage(CC.translate("&aYou have successfully sent this notification."));
                        player.sendMessage(CC.translate("&aPlayer: &b" + specified));
                        player.sendMessage(CC.translate(""));

                        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(specified);
                        OxygenBukkit.getInstance().getProfileManager().sendNotification(profile.getUniqueId(), newMessage);

                        if (create) {
                            Notification notification = new Notification(newMessage);
                            notification.save();
                            OxygenBukkit.getInstance().getNotificationManager().getNotificationMap().put(notification.getId(), notification);
                        }
                    } else {
                        new CreationMenu(newMessage).openMenu(player);
                    }
                }).openMenu(player);
            });
        }

        public void conversationString(Player p, Callback<String> callback) {
            ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

                public String getPromptText(ConversationContext context) {
                    return "";
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String text) {
                    callback.callback(text);
                    if (OxygenBukkit.getInstance().getProfileManager().getProfile(text) == null) {
                        p.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", text));
                        new SelectionMenu(newMessage, create).openMenu(p);
                    }

                    return Prompt.END_OF_CONVERSATION;
                }

            }).withLocalEcho(false).withEscapeSequence("cancel").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");

            Conversation con = factory.buildConversation(p);
            p.beginConversation(con);
        }
    }


    @AllArgsConstructor
    private static class SendAllButton extends Button {
        private final List<String> newMessage;
        boolean create;

        @Override
        public ItemStack getItemStack(Player player) {
            Material material = Material.LAPIS_BLOCK;
            int data = 0;
            String display_name = "&6Send Notification to Everyone";

            return ItemMaker.of(material).setData((short) data).setDisplayName(display_name).setLore(getLore()).build();
        }

        public List<String> getLore() {
            List<String> lore = new ArrayList<>();

            lore.add("");
            lore.add("&7Click here to send this");
            lore.add("&7notification to a all profiles.");

            return lore;
        }
        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new ConfirmationMenu("Send notification?", (b) -> {
                if (b) {
                    player.sendMessage(CC.translate(""));
                    player.sendMessage(CC.translate("&aYou have successfully sent this notification."));
                    player.sendMessage(CC.translate(""));

                    if (create) {
                        Notification notification = new Notification(newMessage);
                        notification.save();
                        OxygenBukkit.getInstance().getNotificationManager().getNotificationMap().put(notification.getId(), notification);
                    }

                    OxygenBukkit.getInstance().getProfileManager().sendNotificationAll(newMessage);
                } else {
                    new CreationMenu(newMessage).openMenu(player);
                }
            }).openMenu(player);
        }
    }
}
