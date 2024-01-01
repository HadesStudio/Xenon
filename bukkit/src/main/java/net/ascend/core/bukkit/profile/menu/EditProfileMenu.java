package net.ascend.core.bukkit.profile.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.menu
//   Date: Monday, July 31, 2023 - 7:56 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.menu.submenus.NotesMenu;
import net.ascend.core.bukkit.profile.menu.submenus.PermissionsMenu;
import net.ascend.core.bukkit.profile.menu.submenus.SiblingsMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.menu.fill.FillerTemplate;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.Menu;
import net.ascend.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class EditProfileMenu extends Menu {
    {
        setClickUpdate(true);
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public int getSize() {
        return XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.size");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.details-button.slot"), new ProfileDetailsButton(profile));
        buttons.put(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.notes-button.slot"), new Notes_Button(profile));
        buttons.put(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.siblings-button.slot"), new Siblings_Button(profile));
        buttons.put(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.permissions-button.slot"), new ProfilePermissionsButton(profile));
        buttons.put(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.ranks-button.slot"), new Ranks_Button(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class ProfileDetailsButton extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.details-button.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.details-button.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.details-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-details-button' is null, using a default value."));
                    return MaterialUtil.HUMAN_SKULL.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-details-button' is broken, using a default value."));
                return MaterialUtil.HUMAN_SKULL.getMaterial();
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.details-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            if (XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getBoolean("selection-menu.buttons.details-button.use-profile-skull")) {
                ItemMaker builder = ItemMaker.of(getMaterial());
                builder.setData((short)3);
                builder.setOwner(profile.getName());
                builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.details-button.display-name").replace("%player_name%", profile.getName()));
                for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("selection-menu.buttons.details-button.lore")) {
                    builder.addLore(line
                            .replace("%player_name%", profile.getName())
                            .replace("%note_size%", String.valueOf(profile.getNotes().size()))
                            .replace("%first_join_date%", TimeUtil.formatDateDiff(profile.getFirstSeen()))
                            .replace("%last_seen_date%", TimeUtil.formatDateDiff(profile.getLastSeen()))
                            .replace("%current_server%", profile.getCurrentServer())
                            .replace("%siblings_size%", String.valueOf(profile.getSiblings().size()))
                    );
                }
                return builder.build();
            } else {
                ItemMaker builder = ItemMaker.of(getMaterial());
                builder.setData(getData());
                builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.details-button.display-name").replace("%player_name%", profile.getName()));
                for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("selection-menu.buttons.details-button.lore")) {
                    builder.addLore(line
                            .replace("%player_name%", profile.getName())
                            .replace("%note_size%", String.valueOf(profile.getNotes().size()))
                            .replace("%first_join_date%", TimeUtil.formatDateDiff(profile.getFirstSeen()))
                            .replace("%last_seen_date%", TimeUtil.formatDateDiff(profile.getLastSeen()))
                            .replace("%current_server%", profile.getCurrentServer())
                            .replace("%siblings_size%", String.valueOf(profile.getSiblings().size()))
                    );
                }
                return builder.build();
            }
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class ProfilePermissionsButton extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.permissions-button.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.permissions-button.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.permissions-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-permissions-button' is null, using a default value."));
                    return Material.REDSTONE;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-permissions-button' is broken, using a default value."));
                return Material.REDSTONE;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.permissions-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.permissions-button.display-name").replace("%player_name%", profile.getName()));
            for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("selection-menu.buttons.permissions-button.lore")) {
                builder.addLore(line
                        .replace("%profile_perms%", String.valueOf(profile.getProfilePermissions().size()))
                        .replace("%bungee_perms%", String.valueOf(profile.getBungeePermissions().size()))
                        .replace("%player_perms%", String.valueOf(profile.getPlayerPermissions().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                new PermissionsMenu(profile, "profile").openMenu(player);
            } else if (clickType.isCreativeAction()) {
                new PermissionsMenu(profile, "bungee").openMenu(player);
            } else {
                new PermissionsMenu(profile, "player").openMenu(player);
            }
        }
    }


    @AllArgsConstructor
    public static class Notes_Button extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.notes-button.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.notes-button.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.notes-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-notes-button' is null, using a default value."));
                    return Material.NAME_TAG;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-notes-button' is broken, using a default value."));
                return Material.NAME_TAG;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.notes-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.notes-button.display-name"));
            for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("selection-menu.buttons.notes-button.lore")) {
                builder.addLore(line
                        .replace("%note_size%", String.valueOf(profile.getNotes().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (player.hasPermission("xenon.staff.notes")) {
                new NotesMenu(profile).openMenu(player);
            } else {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.messages.no-permissions")));
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Siblings_Button extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.siblings-button.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.siblings-button.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.siblings-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-siblings-button' is null, using a default value."));
                    return Material.ENCHANTED_BOOK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-siblings-button' is broken, using a default value."));
                return Material.ENCHANTED_BOOK;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.siblings-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.siblings-button.display-name"));
            for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("selection-menu.buttons.siblings-button.lore")) {
                builder.addLore(line
                        .replace("%siblings_size%", String.valueOf(profile.getSiblings().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (player.hasPermission("xenon.staff.siblings")) {
                new SiblingsMenu(profile).openMenu(player);
            } else {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.messages.no-permissions")));
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Ranks_Button extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.ranks-button.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.ranks-button.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.ranks-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-ranks-button' is null, using a default value."));
                    return Material.NETHER_STAR;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'profile-ranks-button' is broken, using a default value."));
                return Material.NETHER_STAR;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("selection-menu.buttons.ranks-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("selection-menu.buttons.ranks-button.display-name"));
            for (String line : XenonBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("selection-menu.buttons.ranks-button.lore")) {
                builder.addLore(line
                        .replace("%grants_size%", String.valueOf(profile.getGrants().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new GrantMenu(profile).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }
}
