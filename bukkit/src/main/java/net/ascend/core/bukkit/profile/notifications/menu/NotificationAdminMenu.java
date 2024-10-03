package net.ascend.core.bukkit.profile.notifications.menu;

import com.google.gson.reflect.TypeToken;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.notifications.Notification;
import net.ascend.core.bukkit.profile.notifications.menu.submenus.CreationMenu;
import net.ascend.core.bukkit.profile.notifications.menu.submenus.MessageEditorMenu;
import net.ascend.core.bukkit.profile.notifications.menu.submenus.SelectionMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.*;

public class NotificationAdminMenu extends PaginatedMenu {

    @Override
    public String getName() {
        return "Notifications Admin";
    }

    @Override
    public String getTitle(Player player) {
        return "Notifications Admin";
    }

    @Override
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


        menuContent.put(36, new PageButton(-1, this));
        menuContent.put(37, Button.getPlaceholder());
        menuContent.put(38, Button.getPlaceholder());
        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, new NewNotificationButton());
        menuContent.put(41, Button.getPlaceholder());
        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, Button.getPlaceholder());
        menuContent.put(44, new PageButton(1, this));

        return menuContent;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Notification notification : XenonBukkit.getInstance().getNotificationManager().getNotificationMap().values()) {
            buttons.put(buttons.size(), new NotificationButton(notification));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class NotificationButton extends Button {

        private Notification notification;

        public String getMaterial() {
            return "NAME_TAG";
        }

        public short getData() {
            return (short) 0;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName("&6Notification &7#" + notification.getId());
            builder.setLore(Arrays.asList(
                    "",
                    "&6Players w/ Notification&7: &c" + XenonBukkit.getInstance().getNotificationManager().getProfilesWithNotification(notification),
                    "&6Date Sent&7: &c" + TimeUtil.formatIntoCalendarString(new Date(notification.getDate())),
                    "&6Last Update&7: &c" + TimeUtil.formatIntoCalendarString(new Date(notification.getLastUpdate())),
                    "&6Message&7:"
                    ));

            for (String line : notification.getMessage()) {
                builder.addLore(" &7- &f" + line);
            }
            builder.addLore("&7");
            builder.addLore("&7Left-Click to edit this notification.");
            builder.addLore("&7Middle-Click to delete this notification.");
            builder.addLore("&7Right-Click to send this notification.");
            builder.addFakeGlow();
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isRightClick()) {
                new SelectionMenu(notification).openMenu(player);
            } else if (clickType.isCreativeAction()) {
                for (Document document : XenonBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
                    UUID uuid = UUID.fromString(document.getString("uniqueId"));
                    Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(uuid);
                    if (profile == null) {
                        break;
                    }

                    profile.getNotifications().removeIf(notifications -> notifications.getId().equalsIgnoreCase(notification.getId()));
                    profile.save();
                }

                XenonBukkit.getInstance().getNotificationManager().removeNotification(notification);
            } else {
                new MessageEditorMenu(notification).openMenu(player);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction();
        }
    }

    @AllArgsConstructor
    private static class NewNotificationButton extends Button {

        public String getMaterial() {
            return "EMERALD_BLOCK";
        }

        public short getData() {
            return (short) 0;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName("&6New Notification");
            builder.setLore(Collections.singletonList("&7Click here to create a new notification!"));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new CreationMenu(Collections.singletonList("Default message."), true).openMenu(player);
        }
    }
}
