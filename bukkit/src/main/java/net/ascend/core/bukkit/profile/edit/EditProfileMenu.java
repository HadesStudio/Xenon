package net.ascend.core.bukkit.profile.edit;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.edit.submenus.NotesMenu;
import net.ascend.core.bukkit.profile.edit.submenus.PermissionsMenu;
import net.ascend.core.bukkit.profile.edit.submenus.AltsMenu;
import net.ascend.core.bukkit.punishment.menu.HistoryMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class EditProfileMenu extends Menu {

    {
        setClickUpdate(true);
    }

   private final Profile profile;

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.details.slot"), new ProfileDetailsButton(profile));

        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.notes.slot"), new NotesButton(profile));
        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.alts.slot"), new AltsButton(profile));
        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.permissions.slot"), new ProfilePermissionsButton(profile));
        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.grants.slot"), new RanksButton(profile));
        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.history.slot"), new HistoryButton(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class ProfileDetailsButton extends Button {
        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.details.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.details.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getBoolean("profile-menu.buttons.details.use-profile-skull")) {
                ItemMaker builder = ItemMaker.of(getMaterial());
                builder.setData((short)3);
                builder.setOwner(profile.getName());
                builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.details.display_name")
                        .replace("%player_name%", profile.getName()));
                for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("profile-menu.buttons.details.lore")) {
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
            } else {
                ItemMaker builder = ItemMaker.of(getMaterial());
                builder.setData(getData());
                builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.details.display_name").replace("%player_name%", profile.getColoredName()));
                for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("profile-menu.buttons.details.lore")) {
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

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.permissions.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.permissions.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.permissions.display_name").replace("%player_name%", profile.getName()));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("profile-menu.buttons.permissions.lore")) {
                builder.addLore(line
                        .replace("%permissions%", String.valueOf(profile.getProfilePermissions().size())));
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new PermissionsMenu(profile).openMenu(player);
        }
    }


    @AllArgsConstructor
    public static class NotesButton extends Button {
        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.notes.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.notes.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.notes.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("profile-menu.buttons.notes.lore")) {
                builder.addLore(line
                        .replace("%notes%", String.valueOf(profile.getNotes().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new NotesMenu(profile, true).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class AltsButton extends Button {

        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.alts.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.alts.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.alts.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("profile-menu.buttons.alts.lore")) {
                builder.addLore(line.replace("%alts%", String.valueOf(profile.getAlts().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new AltsMenu(profile).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class RanksButton extends Button {
        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.grants.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.grants.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.grants.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("profile-menu.buttons.grants.lore")) {
                builder.addLore(line
                        .replace("%grants%", String.valueOf(profile.getGrants().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new GrantMenu(profile).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class HistoryButton extends Button {
        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.history.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("profile-menu.buttons.history.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("profile-menu.buttons.history.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("profile-menu.buttons.history.lore")) {
                builder.addLore(line
                        .replace("%punishments%", String.valueOf(profile.getPunishments().size()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new HistoryMenu(profile).openMenu(player);
        }
    }
}
