package net.ascend.core.bukkit.profile.myprofile.menus;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.myprofile.menus.type.FriendRequestMenu;
import net.ascend.core.bukkit.profile.myprofile.menus.type.FriendsMenu;
import net.ascend.core.bukkit.profile.myprofile.menus.type.NotificationsMenu;
import net.ascend.core.bukkit.profile.myprofile.menus.type.SocialMediaMenu;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.util.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MainProfileMenu extends Menu {

    private final Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public int getSize() {
        return 5*9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> toReturn = new HashMap<>();

        if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.fill.style").equalsIgnoreCase("FILLED")) {
            for (int i = 0; i < getSize(); ++i) {
                toReturn.put(i, Button.getPlaceholder());
            }
        } else if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.fill.style").equalsIgnoreCase("SPECIFIC")) {
            for (String slot : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("myprofile-menu.fill.slots")) {
                toReturn.put(Integer.parseInt(slot) - 1, Button.getPlaceholder());
            }
        }


        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("myprofile-menu.buttons.social.slot"), new Social_Button(profile));
        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("myprofile-menu.buttons.profile.slot"), new Profile_Button(profile));
        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("myprofile-menu.buttons.friends.slot"), new Friends_Button(profile));
        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("myprofile-menu.buttons.notifications.slot"), new Notifications_Button(profile));


        return toReturn;
    }

    @AllArgsConstructor
    public static class Social_Button extends Button {
        private final Profile player_profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.social.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("myprofile-menu.buttons.social.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.social.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("myprofile-menu.buttons.social.lore")) {
                builder.addLore(line
                        .replace("%total_linked%", String.valueOf(player_profile.getSocialMedia().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new SocialMediaMenu(player_profile).openMenu(player);
        }
    }


    @AllArgsConstructor
    public static class Profile_Button extends Button {

        private final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.profile.material");
        }

        public short getData() {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.profile.material").equalsIgnoreCase("SKULL_ITEM")) {
                return (short) 3;
            } else {
                return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("myprofile-menu.buttons.profile.data");
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.profile.display_name"));
            if (getMaterial().equalsIgnoreCase("SKULL_ITEM")) {
                builder.setOwner(player.getName());
            }

            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("myprofile-menu.buttons.profile.lore")) {
                builder.addLore(line
                        .replace("%player_name%", profile.getName())
                        .replace("%note%", String.valueOf(profile.getNotes().size()))
                        .replace("%first_join%", TimeUtil.formatIntoCalendarString(new Date(profile.getFirstSeen())))
                        .replace("%last_seen%", TimeUtil.formatIntoCalendarString(new Date(profile.getLastSeen())))
                        .replace("%current_server%", profile.getCurrentServer())
                        .replace("%alts%", String.valueOf(profile.getAlts().size()))
                        .replace("%current_rank%", profile.getActiveRank().getName())
                        .replace("%current_tag%", profile.getActiveTag() == null ? "None" : profile.getActiveTag().getName())
                );
            }

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }

    @AllArgsConstructor
    public static class Friends_Button extends Button {

        private final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.friends.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("myprofile-menu.buttons.friends.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.friends.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("myprofile-menu.buttons.friends.lore")) {
                builder.addLore(line
                        .replace("%friends_size%", String.valueOf(profile.getFriends().size()))
                        .replace("%friend_requests_size%", String.valueOf(profile.getFriendRequests().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                new FriendsMenu(profile).openMenu(player);
            } else {
                new FriendRequestMenu(profile).openMenu(player);
            }
        }
    }

    @AllArgsConstructor
    public static class Notifications_Button extends Button {
        private final Profile player_profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.notifications.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("myprofile-menu.buttons.notifications.data");
        }

        public int getAmount() {
            return player_profile.getNotifications().isEmpty() ? 1 : player_profile.getNotifications().size();
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("myprofile-menu.buttons.notifications.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("myprofile-menu.buttons.notifications.lore")) {
                builder.addLore(line
                        .replace("%unread_size%", String.valueOf(player_profile.getNotifications().size()))
                );
            }
            builder.setAmount(getAmount());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new NotificationsMenu(player_profile).openMenu(player);
        }
    }
}
