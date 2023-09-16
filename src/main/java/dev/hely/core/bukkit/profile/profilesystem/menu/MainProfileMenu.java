package dev.hely.core.bukkit.profile.profilesystem.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.menu
//   Date: Monday, July 10, 2023 - 2:22 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.profilesystem.menu.type.FriendRequestMenu;
import dev.hely.core.bukkit.profile.profilesystem.menu.type.FriendsMenu;
import dev.hely.core.bukkit.profile.profilesystem.menu.type.NotificationsMenu;
import dev.hely.core.bukkit.profile.profilesystem.menu.type.SocialMediaMenu;
import dev.hely.lib.util.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MainProfileMenu extends Menu {
    private Profile profile;
    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("title").replace("%player%", profile.getName());
    }

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("size");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        final Map<Integer, Button> toReturn = new HashMap<>();

        if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("placeholders.style").equalsIgnoreCase("FILLED")) {
            for (int i = 0; i < getSize(); ++i) {
                toReturn.put(i, Button.getPlaceholder());
            }
        } else if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("placeholders.style").equalsIgnoreCase("SPECIFIC")) {
            for (String slot : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("placeholders.specific-slots")) {
                toReturn.put(Integer.parseInt(slot) - 1, Button.getPlaceholder());
            }
        }


        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("statistics-button.slot"), new Statistics_Button(profile));
        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-button.slot"), new Social_Button(profile));
        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("profile-button.slot"), new Profile_Button(profile));
        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("friends-button.slot"), new Friends_Button(profile));
        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("notifications-button.slot"), new Notifications_Button(profile));


        return toReturn;
    }

    @AllArgsConstructor
    public static class Statistics_Button extends Button {
        private final Profile player_profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("statistics-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("statistics-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("statistics-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'statistics-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'statistics-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("statistics-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("statistics-button.title"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("statistics-button.lore")) {
                builder.addLore(line
                        .replace("%player%", player_profile.getName())
                        .replace("%total_kills%", String.valueOf(player_profile.getTotalKills()))
                        .replace("%total_deaths%", String.valueOf(player_profile.getTotalDeaths()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }

    @AllArgsConstructor
    public static class Social_Button extends Button {
        private final Profile player_profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'social-button' is null, using a default value."));
                    return Material.PAINTING;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'social-button' is broken, using a default value."));
                return Material.PAINTING;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-button.title"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("social-button.lore")) {
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
        private final Profile player_profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'profile-button' is null, using a default value."));
                    return Material.SIGN;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'profile-button' is broken, using a default value."));
                return Material.SIGN;
            }
        }

        public short getData() {
            if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material")) == Material.SKULL_ITEM) {
                return (short) 3;
            } else {
                return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("profile-button.data");
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            //final Rank activeGrant = player_profile.getActiveHighestRank();
            //List<Grant> grant = player_profile.getGrants().stream().sorted(Comparator.comparingLong(Grant::getAddedTime).reversed()).filter(name -> name.getRankName().equals(activeGrant.getName())).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.title"));
            /*for (String line : OxygenGlobal.getInstance().getProfileMenuConfig().getConfiguration().getStringList("profile-button.lore")) {
                builder.addLore(line
                        .replace("%rank%", activeGrant.getName())
                        .replace("%rank_time%", activeGrant.get)
                );
            }*/
            if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material")) == Material.SKULL_ITEM) {
                builder.setOwner(player.getName());
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }

    @AllArgsConstructor
    public static class Friends_Button extends Button {
        private final Profile player_profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("friends-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("friends-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("friends-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'friends-button' is null, using a default value."));
                    return Material.EYE_OF_ENDER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'friends-button' is broken, using a default value."));
                return Material.EYE_OF_ENDER;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("friends-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("friends-button.title"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("friends-button.lore")) {
                builder.addLore(line
                        .replace("%friends_size%", String.valueOf(player_profile.getFriends().size()))
                        .replace("%friend_requests_size%", String.valueOf(player_profile.getFriendRequests().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                new FriendsMenu(player_profile).openMenu(player);
            } else {
                new FriendRequestMenu(player_profile).openMenu(player);
            }
        }
    }

    @AllArgsConstructor
    public static class Notifications_Button extends Button {
        private final Profile player_profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'notifications-button' is null, using a default value."));
                    return Material.WATCH;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'notifications-button' is broken, using a default value."));
                return Material.WATCH;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("notifications-button.data");
        }

        public int getAmount() {
            return player_profile.getNotifications().isEmpty() ? 1 : player_profile.getNotifications().size();
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications-button.title"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("notifications-button.lore")) {
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
