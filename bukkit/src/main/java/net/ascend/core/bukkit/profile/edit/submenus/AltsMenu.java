package net.ascend.core.bukkit.profile.edit.submenus;

import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.edit.EditProfileMenu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.util.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class AltsMenu extends PaginatedMenu {

    final Profile profile;

    {
        setAutoUpdate(true);
    }

    public AltsMenu(Profile profile) {
        this.profile = profile;

        if (profile.getAlts().isEmpty()) {
            profile.setupAlts();
        }
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("alts-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("alts-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Profile> altProfile = new ArrayList<>();
        profile.getAlts().forEach(uuid -> altProfile.add(XenonBukkit.getInstance().getProfileManager().getProfile(uuid)));

        for (Profile alt : altProfile) {
            buttons.put(buttons.size(), new AltButton(alt));
        }

        return buttons;
    }

    @Override
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

        menuContent.put(18, new PageButton(-1, this));
        menuContent.put(19, Button.getPlaceholder());
        menuContent.put(20, Button.getPlaceholder());
        menuContent.put(21, Button.getPlaceholder());
        menuContent.put(22, Button.getPlaceholder());
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());

        menuContent.put(26, new PageButton(1, this));
        return menuContent;
    }

    @AllArgsConstructor
    public static class AltButton extends Button {

        final Profile profile;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("SKULL_ITEM");
            builder.setData((short) 3);
            builder.setOwner(profile.getName());
            builder.setDisplayName(
                    XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("alts-menu.button.alt.display_name")
                            .replace("%profile_name%", profile.getName())
                            .replace("%profile_color%", profile.getColor()));


            Punishment blacklist = profile.getActiveBlacklist();
            Punishment ban = profile.getActiveBan();
            Punishment mute = profile.getActiveMute();

            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("alts-menu.button.alt.lore")) {
                builder.addLore(line
                        .replace("%last_seen%", TimeUtil.formatIntoCalendarString(new Date(profile.getLastSeen())))
                        .replace("%last_server%", profile.getLastServer())
                        .replace("%blacklist_status%", blacklist == null ? "No" : blacklist.getRemaining())
                        .replace("%ban_status%", ban == null ? "No" : ban.getRemaining())
                        .replace("%mute_status%", mute == null ? "No" : mute.getRemaining())
                        .replace("%warnings%", String.valueOf(profile.getPunishmentCountByType(PunishmentType.MUTE)))
                );
            }

            return builder.build();
        }
    }
}
