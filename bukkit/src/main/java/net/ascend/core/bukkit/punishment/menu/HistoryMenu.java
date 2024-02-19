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
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.title").replace("%player_name%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.blacklist.slot"), new BlacklistButton());
        buttons.put(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.bans.slot"), new BanButton());
        buttons.put(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.kicks.slot"), new KickButton());
        buttons.put(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.mutes.slot"), new MuteButton());
        buttons.put(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.warns.slot"), new WarnButton());

        return buttons;
    }

    @Override
    public int getSize() {
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.size") * 9;
    }

    private class BlacklistButton extends Button {
        public String getMaterial() {
            return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.blacklist.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> blacklists = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BLACKLIST).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.name").replace("%blacklist_size%", String.valueOf(blacklists.size())));
            for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.blacklist.lore")) {
                builder.addLore(line
                        .replace("%status%", profile.getActiveBlacklist() != null ? "&a&nYes" : "&c&nNo")
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.blacklist.settings.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.settings.permission.message")));
                    return;
                }
                new BlacklistsMenu(profile).openMenu(player);
            }
        }
    }


    private class BanButton extends Button {
        public String getMaterial() {
            return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.bans.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> bans = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BAN).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.name").replace("%ban_size%", String.valueOf(bans.size())));
            for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.bans.lore")) {
                builder.addLore(line
                        .replace("%status%", profile.getActiveBan() != null ? "&a&nYes" : "&c&nNo")
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.bans.settings.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.settings.permission.message")));
                    return;
                }
            }
            new BansMenu(profile).openMenu(player);
        }
    }

    private class KickButton extends Button {
        public String getMaterial() {
            return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.kicks.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> kicks = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.KICK).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.name").replace("%kick_size%", String.valueOf(kicks.size())));
            for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.kicks.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.kicks.settings.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.settings.permission.message")));
                    return;
                }
            }
            new KicksMenu(profile).openMenu(player);

        }
    }

    private class MuteButton extends Button {

        public String getMaterial() {
            return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.mutes.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> mutes = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.MUTE).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.name").replace("%mute_size%", String.valueOf(mutes.size())));
            for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.mutes.lore")) {
                builder.addLore(line.replace("%status%", profile.getActiveMute() != null ? "&a&nYes" : "&c&nNo"));
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.mutes.settings.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.settings.permission.message")));
                    return;
                }
            }
            new MutesMenu(profile).openMenu(player);
        }
    }

    private class WarnButton extends Button {
        public String getMaterial() {
            return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.warns.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> warnings = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.WARN).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.name").replace("%warning_size%", String.valueOf(warnings.size())));
            for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.warns.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.warns.settings.permission.enabled")) {
                if (!player.hasPermission(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.settings.permission.message")));
                    return;
                }
            }
            new WarningsMenu(profile).openMenu(player);
        }
    }
}
