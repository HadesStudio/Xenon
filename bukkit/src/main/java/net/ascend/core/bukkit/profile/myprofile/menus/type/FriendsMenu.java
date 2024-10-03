package net.ascend.core.bukkit.profile.myprofile.menus.type;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.myprofile.menus.MainProfileMenu;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.InformationButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class FriendsMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("friends-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("friends-menu.title")
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

        profile.getFriends().forEach(friend -> buttons.put(buttons.size(), new Friend_Button(XenonBukkit.getInstance().getProfileManager().getProfile(friend), profile)));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, Button.getPlaceholder());
        buttons.put(1, Button.getPlaceholder());
        buttons.put(2, Button.getPlaceholder());
        buttons.put(3, Button.getPlaceholder());
        buttons.put(4, Button.getPlaceholder());
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
    private static class Friend_Button extends Button {
        private final Profile target_profile;
        private final Profile player_profile;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("SKULL_ITEM");

            builder.setData((short)3);
            builder.setOwner(target_profile.getName());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("friends-menu.button.display_name").replace("%friend_name%", target_profile.getName()));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("friends-menu.button.lore")) {
                builder.addLore(line
                        .replace("%friend_name%", target_profile.getName())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                player_profile.getFriends().remove(target_profile.getUniqueId());
                target_profile.getFriends().remove(player_profile.getUniqueId());

                player_profile.save();
                target_profile.save();
            } else {
                new MainProfileMenu(target_profile).openMenu(player);
            }
        }
    }
}
