package dev.hely.core.bukkit.punishment.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.punishment.menu
//   Date: Wednesday, September 06, 2023 - 3:54 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.punishment.PunishmentType;
import dev.hely.core.bukkit.punishment.menu.submenu.StaffHistoryViewingMenu;
import dev.hely.core.bukkit.utils.TimeUtil;
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

import java.util.*;

@AllArgsConstructor
public class StaffHistoryMenu extends Menu {
    final Profile profile;
    final Map<UUID, Punishment> executedPunishments;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.buttons.data-button.slot"), new Data_Button(profile, executedPunishments));
        buttons.put(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.buttons.bans-button.slot"), new Bans_Button(profile, executedPunishments));
        buttons.put(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.buttons.warns-button.slot"), new Warns_Button(profile, executedPunishments));
        buttons.put(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.buttons.mutes-button.slot"), new Mutes_Button(profile, executedPunishments));
        buttons.put(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.buttons.pardons-button.slot"), new Pardons_Button(profile, executedPunishments));
        buttons.put(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.buttons.all-button.slot"), new All_Button(profile, executedPunishments));

        return buttons;
    }

    @AllArgsConstructor
    public static class Data_Button extends Button {
        final Profile profile;
        final Map<UUID, Punishment> executedPunishments;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.SKULL_ITEM);
            builder.setData((short)3);
            builder.setOwner(profile.getName());
            builder.setDisplayName(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.buttons.data-button.display-name").replace("%player%", profile.getName()));
            for (String line : OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getStringList("menu.buttons.data-button.lore")) {
                builder.addLore(line
                        .replace("%rank%", String.valueOf(profile.getActiveHighestRank().getName()))
                        .replace("%playtime%", TimeUtil.getMoreDetailedTime(profile.getTotalPlayTime()))
                        .replace("%last_seen%", TimeUtil.formatSimplifiedDateDiff(profile.getLastSeen()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getType() == PunishmentType.BAN || punishment.getValue().getType() == PunishmentType.BLACKLIST) {
                    punishments.add(punishment.getValue());
                }
            }
            new StaffHistoryViewingMenu(profile, punishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Bans_Button extends Button {
        final Profile profile;
        final Map<UUID, Punishment> executedPunishments;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.bans-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.bans-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.bans-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-bans-material' is null, using a default value."));
                    return Material.BOOK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-bans-material' is broken, using a default value."));
                return Material.BOOK;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.button.bans-button.data");
        }

        public int getTotal() {
            int amount = 0;
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getType() == PunishmentType.BAN || punishment.getValue().getType() == PunishmentType.BLACKLIST) {
                    amount = amount + 1;
                }
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.buttons.bans-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getStringList("menu.buttons.bans-button.lore")) {
                builder.addLore(line
                        .replace("%size%", String.valueOf(getTotal()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getType() == PunishmentType.BAN || punishment.getValue().getType() == PunishmentType.BLACKLIST) {
                    punishments.add(punishment.getValue());
                }
            }
            new StaffHistoryViewingMenu(profile, punishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Warns_Button extends Button {
        final Profile profile;
        final Map<UUID, Punishment> executedPunishments;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.warns-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.warns-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.warns-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-warns-material' is null, using a default value."));
                    return Material.BOOK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-warns-material' is broken, using a default value."));
                return Material.BOOK;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.button.warns-button.data");
        }

        public int getTotal() {
            int amount = 0;
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getType() == PunishmentType.WARN) {
                    amount = amount + 1;
                }
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.buttons.warns-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getStringList("menu.buttons.warns-button.lore")) {
                builder.addLore(line
                        .replace("%size%", String.valueOf(getTotal()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getType() == PunishmentType.WARN) {
                    punishments.add(punishment.getValue());
                }
            }
            new StaffHistoryViewingMenu(profile, punishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Mutes_Button extends Button {
        final Profile profile;
        final Map<UUID, Punishment> executedPunishments;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.mutes-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.mutes-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.mutes-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-mutes-material' is null, using a default value."));
                    return Material.BOOK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-mutes-material' is broken, using a default value."));
                return Material.BOOK;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.button.mutes-button.data");
        }

        public int getTotal() {
            int amount = 0;
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getType() == PunishmentType.MUTE) {
                    amount = amount + 1;
                }
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.buttons.mutes-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getStringList("menu.buttons.mutes-button.lore")) {
                builder.addLore(line
                        .replace("%size%", String.valueOf(getTotal()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getType() == PunishmentType.MUTE) {
                    punishments.add(punishment.getValue());
                }
            }
            new StaffHistoryViewingMenu(profile, punishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class Pardons_Button extends Button {
        final Profile profile;
        final Map<UUID, Punishment> executedPunishments;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.pardons-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.pardons-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.pardons-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-pardons-material' is null, using a default value."));
                    return Material.BOOK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-pardons-material' is broken, using a default value."));
                return Material.BOOK;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.button.pardons-button.data");
        }

        public int getTotal() {
            int amount = 0;
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getRemovedBy().equalsIgnoreCase(profile.getName())) {
                    amount = amount + 1;
                }
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.buttons.pardons-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getStringList("menu.buttons.pardons-button.lore")) {
                builder.addLore(line
                        .replace("%size%", String.valueOf(getTotal()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                if (punishment.getValue().getRemovedBy().equalsIgnoreCase(profile.getName())) {
                    punishments.add(punishment.getValue());
                }
            }
            new StaffHistoryViewingMenu(profile, punishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class All_Button extends Button {
        final Profile profile;
        final Map<UUID, Punishment> executedPunishments;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.all-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.all-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.button.all-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-all-material' is null, using a default value."));
                    return Material.BOOK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'staff-history-all-material' is broken, using a default value."));
                return Material.BOOK;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getInt("menu.button.all-button.data");
        }

        public int getTotal() {
            int amount = 0;
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                amount = amount + 1;
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getString("menu.buttons.all-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getStaffHistoryConfig().getConfiguration().getStringList("menu.buttons.all-button.lore")) {
                builder.addLore(line
                        .replace("%size%", String.valueOf(getTotal()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (Map.Entry<UUID, Punishment> punishment : executedPunishments.entrySet()) {
                punishments.add(punishment.getValue());
            }
            new StaffHistoryViewingMenu(profile, punishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }
}

