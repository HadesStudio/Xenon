package dev.hely.core.bukkit.profile.profilesystem.menu.type;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.menu.type
//   Date: Saturday, July 15, 2023 - 6:07 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.profilesystem.menu.MainProfileMenu;
import dev.hely.core.bukkit.utils.material.MaterialUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.InformationButton;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class FriendRequestMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("friends.friends-request-menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("friends.friends-request-menu.title");
    }

    @Override
    public int getSize() {
        return 9*5;
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
        buttons.put(4, Button.getPlaceholder());

        buttons.put(9, Button.getPlaceholder());
        buttons.put(17, Button.getPlaceholder());
        buttons.put(18, Button.getPlaceholder());
        buttons.put(26, Button.getPlaceholder());
        buttons.put(27, Button.getPlaceholder());
        buttons.put(35, Button.getPlaceholder());

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

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        profile.getFriendRequests().forEach(friend -> buttons.put(buttons.size(), new Friend_Request_Button(OxygenBukkit.getInstance().getProfileManager().getProfile(friend), profile)));

        return buttons;
    }

    @AllArgsConstructor
    private static class Friend_Request_Button extends Button {
        private final Profile target_profile;
        private final Profile player_profile;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(MaterialUtil.HUMAN_SKULL.getMaterial());
            builder.setData((short)3);
            builder.setOwner(target_profile.getName());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("friends.friends-request-menu.button.name").replace("%player_name%", target_profile.getName()));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("friends.friends-request-menu.button.lore")) {
                builder.addLore(line
                        .replace("%player%", target_profile.getName())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                OxygenBukkit.getInstance().getProfileManager().acceptFriendRequest(player_profile, Bukkit.getPlayer(player_profile.getName()), target_profile.getUniqueId());
            } else {
                new MainProfileMenu(target_profile).openMenu(player);
            }
        }
    }
}
