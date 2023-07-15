package dev.hely.core.profile.profilesystem.menu;

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

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import dev.hely.lib.CC;
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
        return Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        final Map<Integer, Button> toReturn = new HashMap<>();

        toReturn.put(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("statistics-button.slot"), new Statistics_Button(profile));
        toReturn.put(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-button.slot"), new Social_Button(profile));
        toReturn.put(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("profile-button.slot"), new Profile_Button(profile));
        toReturn.put(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("friends-button.slot"), new Friends_Button(profile));
        toReturn.put(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("notifications-button.slot"), new Notifications_Button(profile));


        return toReturn;
    }

    @AllArgsConstructor
    public static class Statistics_Button extends Button {
        private final Profile player_profile;

        public Material getMaterial() {
            if (Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("statistics-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("statistics-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("statistics-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'statistics-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'statistics-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("statistics-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("statistics-button.title"));
            for (String line : Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getStringList("statistics-button.lore")) {
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
            if (Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("social-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("social-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("social-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'social-button' is null, using a default value."));
                    return Material.PAINTING;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'social-button' is broken, using a default value."));
                return Material.PAINTING;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("social-button.title"));
            for (String line : Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getStringList("social-button.lore")) {
                builder.addLore(line
                        .replace("%total_linked%", String.valueOf(player_profile.getSocialMedia().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }


    @AllArgsConstructor
    public static class Profile_Button extends Button {
        private final Profile player_profile;

        public Material getMaterial() {
            if (Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'profile-button' is null, using a default value."));
                    return Material.SIGN;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'profile-button' is broken, using a default value."));
                return Material.SIGN;
            }
        }

        public short getData() {
            if (Material.getMaterial(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material")) == Material.SKULL_ITEM) {
                return (short) 3;
            } else {
                return (short) Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("profile-button.data");
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            //List<Grant> activeGrants = new ArrayList<>(Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId()).getGrants());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.title"));
            for (String line : Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getStringList("profile-button.lore")) {
                /*builder.addLore(line
                        .replace("%rank%", activeGrants))
                );*/
            }
            if (Material.getMaterial(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("profile-button.material")) == Material.SKULL_ITEM) {
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
            if (Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("friends-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("friends-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("friends-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'friends-button' is null, using a default value."));
                    return Material.EYE_OF_ENDER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'friends-button' is broken, using a default value."));
                return Material.EYE_OF_ENDER;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("friends-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("friends-button.title"));
            for (String line : Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getStringList("friends-button.lore")) {
                builder.addLore(line
                        .replace("%friends_size%", String.valueOf(player_profile.getFriends().size()))
                        .replace("%friend_requests_size%", String.valueOf(player_profile.getFriendRequests().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }

    @AllArgsConstructor
    public static class Notifications_Button extends Button {
        private final Profile player_profile;

        public Material getMaterial() {
            if (Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'notifications-button' is null, using a default value."));
                    return Material.WATCH;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'notifications-button' is broken, using a default value."));
                return Material.WATCH;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getInt("notifications-button.data");
        }

        public int getAmount() {
            return player_profile.getNotifications().isEmpty() ? 1 : player_profile.getNotifications().size();
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getString("notifications-button.title"));
            for (String line : Oxygen.getInstance().getProfileMenuConfig().getConfiguration().getStringList("notifications-button.lore")) {
                builder.addLore(line
                        .replace("%unread_size%", String.valueOf(player_profile.getNotifications().size()))
                );
            }
            builder.setAmount(getAmount());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }


}
