package dev.hely.core.punishment.menu;

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.core.punishment.menu.types.*;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class HistoryMenu extends Menu {
    private Profile profile;

    @Override
    public String getName() {
        return Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.title").replace("%player_name%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.blacklist.slot"), new BlacklistButton());
        buttons.put(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.bans.slot"), new BanButton());
        buttons.put(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.kicks.slot"), new KickButton());
        buttons.put(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.mutes.slot"), new MuteButton());
        buttons.put(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.warns.slot"), new WarnButton());

        return buttons;
    }

    @Override
    public int getSize() {
        return Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.size") * 9;
    }

    private class BlacklistButton extends Button {
        public Material getMaterial() {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'blacklist-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'blacklist-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.blacklist.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> blacklists = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BLACKLIST).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.name").replace("%blacklist_size%", String.valueOf(blacklists.size())));
            for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.blacklist.lore")) {
                builder.addLore(line
                        .replace("%status%", profile.getActiveBlacklist() != null ? "&a&nYes" : "&c&nNo")
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.blacklist.settings.permission.enabled")) {
                if (!player.hasPermission(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.blacklist.settings.permission.message")));
                    return;
                }
                new BlacklistsMenu(profile).openMenu(player);
            }
        }
    }


    private class BanButton extends Button {
        public Material getMaterial() {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'bans-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'bans-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.bans.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> bans = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BAN).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.name").replace("%ban_size%", String.valueOf(bans.size())));
            for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.bans.lore")) {
                builder.addLore(line
                        .replace("%status%", profile.getActiveBan() != null ? "&a&nYes" : "&c&nNo")
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.bans.settings.permission.enabled")) {
                if (!player.hasPermission(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.bans.settings.permission.message")));
                    return;
                }
            }
            new BansMenu(profile).openMenu(player);
        }
    }

    private class KickButton extends Button {
        public Material getMaterial() {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'kicks-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'kicks-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.kicks.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> kicks = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.KICK).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.name").replace("%kick_size%", String.valueOf(kicks.size())));
            for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.kicks.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.kicks.settings.permission.enabled")) {
                if (!player.hasPermission(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.kicks.settings.permission.message")));
                    return;
                }
            }
            new KicksMenu(profile).openMenu(player);

        }
    }

    private class MuteButton extends Button {
        public Material getMaterial() {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'mutes-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'mutes-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.mutes.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> mutes = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.MUTE).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.name").replace("%mute_size%", String.valueOf(mutes.size())));
            for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.mutes.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.mutes.settings.permission.enabled")) {
                if (!player.hasPermission(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.mutes.settings.permission.message")));
                    return;
                }
            }
            new MutesMenu(profile).openMenu(player);
        }
    }

    private class WarnButton extends Button {
        public Material getMaterial() {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'warns-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'warns-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("selector.warns.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> warnings = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.WARN).collect(Collectors.toList());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.name").replace("%warning_size%", String.valueOf(warnings.size())));
            for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("selector.warns.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getBoolean("selector.warns.settings.permission.enabled")) {
                if (!player.hasPermission(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.settings.permission.permission"))) {
                    player.sendMessage(CC.translate(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("selector.warns.settings.permission.message")));
                    return;
                }
            }
            new WarningsMenu(profile).openMenu(player);
        }
    }
}
