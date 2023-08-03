package dev.hely.core.tag.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.tag.menu
//   Date: Wednesday, August 02, 2023 - 8:14 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import dev.hely.core.tag.Tag;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TagMenu extends PaginatedMenu {
    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.title");
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
        menuContent.put(9, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());
        menuContent.put(18, Button.getPlaceholder());
        menuContent.put(27, Button.getPlaceholder());
        menuContent.put(26, Button.getPlaceholder());
        menuContent.put(35, Button.getPlaceholder());
        menuContent.put(36, new PageButton(-1, this));
        menuContent.put(37, Button.getPlaceholder());
        menuContent.put(38, Button.getPlaceholder());
        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, new ResetButton(player));
        menuContent.put(41, Button.getPlaceholder());
        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, Button.getPlaceholder());
        menuContent.put(44, new PageButton(1, this));


        return menuContent;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Tag tags : Oxygen.getInstance().getTagManager().getTags()) {
            buttons.put(buttons.size(), new TagsButton(player, tags));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class TagsButton extends Button {
        Player player;
        Tag tag;

        public Material getMaterial() {
            if (Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.tag-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.tag-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.tag-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'tag-button' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'tag-button' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getInt("menu.buttons.tag-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getName());

            if (player.hasPermission("oxygen.tag." + tag.getName())) {
                ItemMaker builder = ItemMaker.of(getMaterial());
                builder.setData(getData());
                builder.setDisplayName(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.tag-button.display-name")
                        .replace("%tag_name%", tag.getName())
                        .replace("%tag_prefix%", tag.getPrefix())
                );
                for (String line : Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getStringList("menu.buttons.tag-button.lores.has-permission")) {
                    builder.addLore(line
                            .replace("%tag_prefix%", tag.getPrefix())
                    );
                }
                if (profile.getTag() != null) {
                    if (profile.getTag().equals(tag.getName())) {
                        builder.addFakeGlow();
                    }
                }
                return builder.build();
            } else {
                ItemMaker builder = ItemMaker.of(getMaterial());
                builder.setData(getData());
                builder.setDisplayName(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.tag-button.display-name")
                        .replace("%tag_name%", tag.getName())
                        .replace("%tag_prefix%", tag.getPrefix())
                );
                for (String line : Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getStringList("menu.buttons.tag-button.lores.no-permission")) {
                    builder.addLore(line);
                }
                if (profile.getTag() != null) {
                    if (profile.getTag().equals(tag.getName())) {
                        builder.addFakeGlow();
                    }
                }
                return builder.build();
            }
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getName());

            if (profile.getTag() == null) {
                profile.setTag(tag.getName());
                profile.save();
                player.sendMessage(CC.translate(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.activated-tag")));
            } else if (profile.getTag().equals(tag.getName())) {
                profile.setTag(null);
                profile.save();
                player.sendMessage(CC.translate(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.reset-tag")));
            } else {
                player.sendMessage(CC.translate(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.need-reset")));
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    private static class ResetButton extends Button {
        Player player;

        public Material getMaterial() {
            if (Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.reset-button.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.reset-button.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.reset-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'reset-button' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'reset-button' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getInt("menu.buttons.reset-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getName());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.buttons.reset-button.display-name"));
            for (String line : Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getStringList("menu.buttons.reset-button.lore")) {
                builder.addLore(line
                        .replace("%tag_name%", getTag(profile))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getName());

            if (profile.getTag() != null) {
                profile.setTag(null);
                profile.save();
            }

            player.sendMessage(CC.translate(Oxygen.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.reset-tag")));
            player.updateInventory();
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }

        public String getTag(Profile profile) {
            if (profile.getTag() == null) {
                return "No Tag";
            } else {
                return profile.getTag();
            }
        }
    }
}
