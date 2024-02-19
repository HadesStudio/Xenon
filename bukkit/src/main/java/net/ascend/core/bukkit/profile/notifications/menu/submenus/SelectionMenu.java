package net.ascend.core.bukkit.profile.notifications.menu.submenus;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.notifications.Notification;
import net.ascend.core.bukkit.lib.callback.Callback;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import net.ascend.core.bukkit.lib.menu.implement.ConfirmationMenu;
import net.ascend.core.bukkit.lib.CC;
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
    Notification notification;

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

        buttons.put(12, new SendPlayerButton(notification));
        buttons.put(14, new SendAllButton(notification));

        return buttons;
    }

    @AllArgsConstructor
    private static class SendPlayerButton extends Button {
        Notification notification;

        @Override
        public ItemStack getItemStack(Player player) {
            String material = "LAPIS_BLOCK";
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
            conversationString(player, (specified) -> new ConfirmationMenu("Send notification?", (b) -> {
                if (b) {
                    player.sendMessage(CC.translate(""));
                    player.sendMessage(CC.translate("&aYou have successfully sent this notification."));
                    player.sendMessage(CC.translate("&aPlayer: &b" + specified));
                    player.sendMessage(CC.translate(""));

                    Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(specified);
                    XenonBukkit.getInstance().getProfileManager().sendNotification(profile.getUniqueId(), notification.getMessage(), notification.getId());
                } else {
                    player.closeInventory();
                }
            }).openMenu(player));
        }

        public void conversationString(Player p, Callback<String> callback) {
            ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

                public String getPromptText(ConversationContext context) {
                    return "";
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String text) {
                    callback.callback(text);
                    if (XenonBukkit.getInstance().getProfileManager().getProfile(text) == null) {
                        p.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", text));
                        new SelectionMenu(notification).openMenu(p);
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
        Notification notification;

        @Override
        public ItemStack getItemStack(Player player) {
            String material = "LAPIS_BLOCK";
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

                    XenonBukkit.getInstance().getProfileManager().sendNotificationAll(notification.getMessage(), notification.getId());
                } else {
                    player.closeInventory();
                }
            }).openMenu(player);
        }
    }
}
