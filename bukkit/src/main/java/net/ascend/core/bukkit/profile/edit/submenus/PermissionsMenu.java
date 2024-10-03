package net.ascend.core.bukkit.profile.edit.submenus;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.menu.button.implement.PageFilterButton;
import net.ascend.core.bukkit.lib.menu.implement.ConfirmationMenu;
import net.ascend.core.bukkit.lib.menu.implement.FilterablePaginatedMenu;
import net.ascend.core.bukkit.lib.menu.implement.PageFilter;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.edit.EditProfileMenu;
import net.ascend.core.bukkit.profile.permission.ProfilePermission;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PermissionsMenu extends FilterablePaginatedMenu<ProfilePermission> {

    final Profile profile;

    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("permission-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("permission-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new BackButton(new EditProfileMenu(profile)));
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());
        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());
        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(36, new PageButton(-1, this));
        menuContent.put(37, Button.getPlaceholder());
        menuContent.put(38, Button.getPlaceholder());

        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, new PageFilterButton<>(this));
        menuContent.put(41, Button.getPlaceholder());

        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, Button.getPlaceholder());
        menuContent.put(44, new PageButton(1, this));

        return menuContent;
    }

    @Override
    public List<PageFilter<ProfilePermission>> generateFilters() {
        List<PageFilter<ProfilePermission>> filters = Lists.newArrayList();

        filters.add(new PageFilter<>("Filter: All Permissions", (permission -> permission != null), true));
        filters.add(new PageFilter<>("Filter: Permanent Permissions", (ProfilePermission::isPermanent), false));
        filters.add(new PageFilter<>("Filter: Temporary Permissions", (permission -> !permission.isPermanent()), false));

        return filters;
    }

    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        loop:
        for (ProfilePermission profilePermission : profile.getProfilePermissions()) {
            for (PageFilter<ProfilePermission> filter : getFilters()) {
                if (filter.test(profilePermission)) {
                    buttons.put(buttons.size(), new ProfilePermissionButton(profile, profilePermission));
                    continue loop;
                }
            }
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class ProfilePermissionButton extends Button {

        final Profile profile;
        final ProfilePermission perm;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("permission-menu.buttons.permission.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("permission-menu.buttons.permission.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("permission-menu.buttons.permission.display_name")
                    .replace("%permission%", perm.getPermission())
                    .replace("%status%", String.valueOf(perm.isActive())));

            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("permission-menu.buttons.permission.lore")) {
                builder.addLore(line
                        .replace("%expires%", perm.isPermanent() ? "Never" : perm.getRemaining())
                        .replace("%status%", String.valueOf(perm.isActive()))
                        .replace("%server%", perm.isGlobal() ? "Global" : perm.getServer())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            new ConfirmationMenu("Remove permission?",(b) -> {
                if (b) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-command.remove-permission.removed")
                            .replace("%player%", profile.getName())
                            .replace("%permission%", perm.getPermission())));

                    List<ProfilePermission> newPermList = profile.getProfilePermissions();
                    for (ProfilePermission profilePermission : newPermList) {
                        if (profilePermission.equals(perm)) {
                            newPermList.remove(perm);
                            return;
                        }
                    }

                    profile.setProfilePermissions(newPermList);
                    profile.save();

                    new PermissionsMenu(profile).openMenu(player);
                } else {
                    new PermissionsMenu(profile).openMenu(player);
                }
            }).openMenu(player);
        }
    }
}
