package dev.hely.core.global.profile.profilesystem.menu.type;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.menu.type
//   Date: Saturday, July 15, 2023 - 6:40 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.global.profile.Profile;
import dev.hely.core.global.profile.profilesystem.impl.Notification;
import dev.hely.core.bukkit.utils.TimeFormatUtils;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.InformationButton;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class NotificationsMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.title");
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int amount = 0;

        final List<Notification> sortedNotifications = profile.getNotifications();
        sortedNotifications.sort(Comparator.comparingLong(Notification::getSentdate).reversed());

        for (Notification notification : sortedNotifications) {
            amount++;
            int finalAmount = amount;
            buttons.put(buttons.size(), new Notification_Button(profile, amount, notification));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, Button.getPlaceholder());
        buttons.put(1, Button.getPlaceholder());
        buttons.put(2, Button.getPlaceholder());
        buttons.put(3, Button.getPlaceholder());
        buttons.put(5, Button.getPlaceholder());
        buttons.put(6, Button.getPlaceholder());
        buttons.put(7, Button.getPlaceholder());
        buttons.put(8, Button.getPlaceholder());
        buttons.put(4, new Read_All_Button(profile));
        buttons.put(36, Button.getPlaceholder());
        buttons.put(37, new PageButton(-1, this));
        buttons.put(38, Button.getPlaceholder());
        buttons.put(39, Button.getPlaceholder());
        buttons.put(40, new InformationButton(this));
        buttons.put(41, Button.getPlaceholder());
        buttons.put(42, Button.getPlaceholder());
        buttons.put(43, new PageButton(1, this));
        buttons.put(44, Button.getPlaceholder());

        return buttons;
    }

    @AllArgsConstructor
    public static class Notification_Button extends Button {
        private final Profile player_profile;
        private final int amount;
        private final Notification notification;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.notification.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.notification.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.notification.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'notification-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'notification-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("notifications.notification-menu.notification.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.notification.name").replace("%number%", String.valueOf(amount)).replace("%date%", TimeFormatUtils.formatIntoCalendarString(new Date(notification.getSentdate()))));
            for (String line : notification.getNotification()) {
                builder.addLore(line);
            }
            builder.addLore("");
            builder.addLore(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.notification.read-line-lore"));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.notifications.read")));
            player_profile.getNotifications().remove(notification);
            player_profile.save();
        }
    }

    @AllArgsConstructor
    public static class Read_All_Button extends Button {
        private final Profile player_profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.read-all.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.read-all.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.read-all.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'read-all-button' is null, using a default value."));
                    return Material.NETHER_STAR;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'read-all-button' is broken, using a default value."));
                return Material.NETHER_STAR;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("notifications.notification-menu.read-all.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications.notification-menu.read-all.name"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("notifications.notification-menu.read-all.lore")) {
                builder.addLore(line
                        .replace("%notification_size%", String.valueOf(player_profile.getNotifications().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.notifications.read-all")));
            player_profile.getNotifications().clear();
            player_profile.save();
        }
    }
}
