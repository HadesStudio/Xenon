package net.ascend.core.bukkit.profile.myprofile.menus.type;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.notifications.Notification;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.InformationButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class NotificationsMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notification-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notification-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int amount = 0;

        List<Notification> sortedNotifications = profile.getNotifications();
        sortedNotifications.sort(Comparator.comparingLong(Notification::getDate).reversed());

        for (Notification noti : sortedNotifications) {
            amount++;
            buttons.put(buttons.size(), new Notification_Button(profile, amount, noti));
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
        buttons.put(4, new Read_All_Button(profile));
        buttons.put(5, Button.getPlaceholder());
        buttons.put(6, Button.getPlaceholder());
        buttons.put(7, Button.getPlaceholder());
        buttons.put(8, Button.getPlaceholder());


        buttons.put(36, new PageButton(-1, this));
        buttons.put(37, Button.getPlaceholder());
        buttons.put(38, Button.getPlaceholder());
        buttons.put(39, Button.getPlaceholder());
        buttons.put(40, new InformationButton(this));
        buttons.put(41, Button.getPlaceholder());
        buttons.put(42, Button.getPlaceholder());
        buttons.put(43, Button.getPlaceholder());
        buttons.put(44, new PageButton(1, this));

        return buttons;
    }

    @AllArgsConstructor
    public static class Notification_Button extends Button {

        private final Profile player_profile;
        private final int amount;
        private final Notification notification;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notification-menu.buttons.notification.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("notification-menu.buttons.notification.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notification-menu.buttons.notification.display_name").replace("%number%", String.valueOf(amount)).replace("%date%", TimeUtil.formatIntoCalendarString(new Date(notification.getDate()))));

            for (String line : notification.getMessage()) {
                builder.addLore("&f" + line);
            }

            builder.addLore("");
            builder.addLore(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notification-menu.buttons.notification.read-line-lore"));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notification.read")));

            player_profile.getNotifications().remove(notification);
            player_profile.save();
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }

    @AllArgsConstructor
    public static class Read_All_Button extends Button {

        private final Profile player_profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notification-menu.buttons.read-all.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("notification-menu.buttons.read-all.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("notification-menu.buttons.read-all.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("notification-menu.buttons.read-all.lore")) {
                builder.addLore(line
                        .replace("%notification_size%", String.valueOf(player_profile.getNotifications().size()))
                );
            }

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("notification.read-all")));

            player_profile.getNotifications().clear();
            player_profile.save();
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }
}
