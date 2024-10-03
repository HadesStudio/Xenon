package net.ascend.core.bukkit.punishment.menu;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.punishment.menu.types.*;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name Xenon
 */
@AllArgsConstructor
public class HistoryMenu extends Menu {

    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.blacklist.slot"), new BlacklistButton());
        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.bans.slot"), new BanButton());
        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.kicks.slot"), new KickButton());
        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.mutes.slot"), new MuteButton());
        buttons.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.warns.slot"), new WarnButton());

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    private class BlacklistButton extends Button {

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.blacklist.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.blacklist.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> blacklists = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BLACKLIST).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.blacklist.name")
                    .replace("%blacklist_size%", String.valueOf(blacklists.size())));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("history-menu.buttons.blacklist.lore")) {
                builder.addLore(line
                        .replace("%status%", profile.getActiveBlacklist() != null ? "&a&nYes" : "&c&nNo")
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getBoolean("history-menu.buttons.blacklist.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.blacklist.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.blacklist.permission.message")));
                    return;
                }
                new BlacklistsMenu(profile).openMenu(player);
            }
        }
    }


    private class BanButton extends Button {
        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.bans.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.bans.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> bans = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BAN).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.bans.name")
                    .replace("%ban_size%", String.valueOf(bans.size())));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("history-menu.buttons.bans.lore")) {
                builder.addLore(line
                        .replace("%status%", profile.getActiveBan() != null ? "&a&nYes" : "&c&nNo")
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getBoolean("history-menu.buttons.bans.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.bans.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.bans.permission.message")));
                    return;
                }
            }
            new BansMenu(profile).openMenu(player);
        }
    }

    private class KickButton extends Button {
        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.kicks.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.kicks.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> kicks = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.KICK).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.kicks.name")
                    .replace("%kick_size%", String.valueOf(kicks.size())));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("history-menu.buttons.kicks.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getBoolean("history-menu.buttons.kicks.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.kicks.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.kicks.permission.message")));
                    return;
                }
            }
            new KicksMenu(profile).openMenu(player);

        }
    }

    private class MuteButton extends Button {

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.mutes.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.mutes.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> mutes = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.MUTE).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.mutes.name")
                    .replace("%mute_size%", String.valueOf(mutes.size())));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("history-menu.buttons.mutes.lore")) {
                builder.addLore(line.replace("%status%", profile.getActiveMute() != null ? "&a&nYes" : "&c&nNo"));
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getBoolean("history-menu.buttons.mutes.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.mutes.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.mutes.permission.message")));
                    return;
                }
            }
            new MutesMenu(profile).openMenu(player);
        }
    }

    private class WarnButton extends Button {
        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.warns.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("history-menu.buttons.warns.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> warnings = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.WARN).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.warns.name")
                    .replace("%warning_size%", String.valueOf(warnings.size())));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("history-menu.buttons.warns.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getBoolean("selector.warns.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.warns.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("history-menu.buttons.warns.permission.message")));
                    return;
                }
            }

            new WarningsMenu(profile).openMenu(player);
        }
    }
}
