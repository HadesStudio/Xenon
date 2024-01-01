package net.ascend.core.bukkit.tag.menu;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.menu.data.button.CustomButton;
import net.ascend.core.bukkit.tag.menu.data.category.CustomCategory;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.Menu;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 10/05/2022
 */
@AllArgsConstructor
public class CustomCategoryMenu extends Menu {
    {
        setClickUpdate(true);
    }

    private final CustomCategory customCategory;

    @Override
    public String getName() {
        return customCategory.getTitle();
    }

    @Override
    public String getTitle(Player player) {
        return customCategory.getTitle();
    }

    @Override
    public int getSize() {
        return customCategory.getRows() * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (customCategory.isFill_enabled()) {
            if (customCategory.getFill_style().equals("FILLED")) {
                for (int i = 0; i < getSize(); ++i) {
                    buttons.put(i, new SeparatorButton(customCategory));
                }
            } else if (customCategory.getFill_style().equals("CORNERS")) {
                for (String slot : customCategory.getFill_corners()) {
                    buttons.put(Integer.parseInt(slot) - 1, new SeparatorButton(customCategory));
                }
            }
        }

        if (customCategory.isBack_enabled()) {
            buttons.put(customCategory.getBack_slot() - 1, new BackButton(customCategory));
        }
        if (customCategory.isReset_tag_enabled()) {
            buttons.put(customCategory.getReset_tag_slot() - 1, new ResetTagButton(customCategory));
        }

        for (CustomButton customButton : customCategory.getCustomButtons()) {
            Tag tag = XenonBukkit.getInstance().getTagManager().getTag(customButton.getTagName());
            buttons.put(customButton.getSlot() - 1, new ItemButton(customButton, tag));
        }

        return buttons;
    }



    @AllArgsConstructor
    private static class BackButton extends Button {

        private CustomCategory customCategory;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.valueOf(customCategory.getBack_material()))
                    .setDisplayName(customCategory.getBack_name())
                    .setData((short) customCategory.getBack_data());

            for (String string : customCategory.getBack_lore()) {
                itemMaker.addLore(string.replace("%tag_name%", getTag(XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()))));
            }

            if (customCategory.isFill_glow()) {
                itemMaker.addFakeGlow();
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new CustomMenu().openMenu(player);
        }
    }

    @AllArgsConstructor
    private static class ResetTagButton extends Button {
        private CustomCategory customCategory;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.valueOf(customCategory.getReset_tag_material()))
                    .setDisplayName(customCategory.getReset_tag_name())
                    .setData((short) customCategory.getReset_tag_data());

            for (String string : customCategory.getReset_tag_lore()) {
                itemMaker.addLore(string.replace("%tag_name%", getTag(XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()))));
            }

            if (customCategory.isFill_glow()) {
                itemMaker.addFakeGlow();
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

            if (profile.getTag() != null) {
                profile.setTag(null);
                profile.save();
            }

            player.sendMessage(CC.translate(XenonBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.reset-tag")));
            player.updateInventory();
        }
    }

    @AllArgsConstructor
    private static class SeparatorButton extends Button {

        private final CustomCategory customCategory;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.valueOf("STAINED_GLASS_PANE")).setDisplayName("&8")
                    .setData((short) customCategory.getFill_data());

            if (customCategory.isFill_glow()) {
                itemMaker.addFakeGlow();
            }
            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }

    @AllArgsConstructor
    private static class ItemButton extends Button {
        private CustomButton customButton;
        private Tag tag;

        @Override
        public ItemStack getItemStack(Player player) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
            ItemMaker builder = ItemMaker.of(customButton.getButton().getType());

            builder.setDisplayName(customButton.getButton().getItemMeta().getDisplayName());
            builder.setData(customButton.getButton().getDurability());
            builder.setAmount(customButton.getButton().getAmount());

            if (player.hasPermission(tag.getPermission())) {
                for (String value : customButton.getOwnsLore()) {
                    builder.addLore(value
                            .replace("%d_arrow%", "\u00BB")
                            .replace("%tag_format%", tag.getPrefix())
                            .replace("%player%", player.getName())
                    );
                }
            } else {
                for (String value : customButton.getNoPermsLore()) {
                    builder.addLore(value
                            .replace("%d_arrow%", "\u00BB")
                            .replace("%tag_format%", tag.getPrefix())
                            .replace("%player%", player.getName())
                    );
                }
            }
            if (profile.getTag() != null) {
                if (profile.getTag().equals(tag.getName())) {
                    builder.addFakeGlow();
                }
            }

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isRightClick()) {

            } else {
                customButton.execute(player);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    public static String getTag(Profile profile) {
        if (profile.getTag() == null) {
            return "No Tag";
        } else {
            return profile.getTag();
        }
    }
}
