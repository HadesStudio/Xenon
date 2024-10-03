package net.ascend.core.bukkit.punishment.menu;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.punishment.menu.staff.StaffHistoryViewingMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class StaffHistoryMenu extends Menu {

    final Profile profile;
    final Map<UUID, List<Punishment>> executedPunishments;

    @Override
    public String getName() {
        return "Staff History";
    }

    @Override
    public String getTitle(Player player) {
        String title = XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("staffhistory-menu.title");
        return title
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        ConfigurationSection buttonsSection = XenonBukkit.getInstance().getMenuConfig().getConfiguration().getConfigurationSection("staffhistory-menu.buttons");

        if (buttonsSection != null) {
            for (String key : buttonsSection.getKeys(false)) {
                ConfigurationSection buttonSection = buttonsSection.getConfigurationSection(key);
                if (buttonSection == null) continue;

                int slot = buttonSection.getInt("slot");

                switch (key) {
                    case "profile":
                        buttons.put(slot, new DataButton(profile, executedPunishments, buttonSection));
                        break;
                    case "bans":
                        buttons.put(slot, new BansButton(profile, executedPunishments, buttonSection));
                        break;
                    case "mutes":
                        buttons.put(slot, new MutesButton(profile, executedPunishments, buttonSection));
                        break;
                    case "warnings":
                        buttons.put(slot, new WarnsButton(profile, executedPunishments, buttonSection));
                        break;
                    case "pardon":
                        buttons.put(slot, new PardonsButton(profile, executedPunishments, buttonSection));
                        break;
                    case "all_punishments":
                        buttons.put(slot, new AllButton(profile, executedPunishments, buttonSection));
                        break;
                }
            }
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class DataButton extends Button {

        final Profile profile;
        final Map<UUID, List<Punishment>> executedPunishments;
        final ConfigurationSection config;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("SKULL_ITEM");
            builder.setData((short) 3);
            builder.setOwner(profile.getName());
            builder.setDisplayName(config.getString("display_name").replace("%player%", profile.getName()));
            List<String> lore = new ArrayList<>();
            for (String line : config.getStringList("lore")) {
                lore.add(line
                        .replace("%current_rank%", profile.getActiveRank().getName())
                        .replace("%playtime%", TimeUtil.getMoreDetailedTime(profile.getTotalPlayTime()))
                        .replace("%last_seen%", TimeUtil.formatIntoCalendarString(new Date(profile.getLastSeen())))
                );
            }
            builder.setLore(lore);
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.BLACKLIST) {
                        punishments.add(punishment);
                    }
                }
            }

            new StaffHistoryViewingMenu(profile, punishments, executedPunishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class BansButton extends Button {

        final Profile profile;
        final Map<UUID, List<Punishment>> executedPunishments;
        final ConfigurationSection config;

        public String getMaterial() {
            return config.getString("material");
        }

        public short getData() {
            return (short) config.getInt("data");
        }

        public int getTotal() {
            int amount = 0;
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.BLACKLIST) {
                        amount++;
                    }
                }
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(config.getString("display_name"));
            List<String> lore = new ArrayList<>();
            for (String line : config.getStringList("lore")) {
                lore.add(line.replace("%total%", String.valueOf(getTotal())));
            }
            builder.setLore(lore);
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.BLACKLIST) {
                        punishments.add(punishment);
                    }
                }
            }
            new StaffHistoryViewingMenu(profile, punishments, executedPunishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class WarnsButton extends Button {
        final Profile profile;
        final Map<UUID, List<Punishment>> executedPunishments;
        final ConfigurationSection config;

        public String getMaterial() {
            return config.getString("material");
        }

        public short getData() {
            return (short) config.getInt("data");
        }

        public int getTotal() {
            int amount = 0;
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getType() == PunishmentType.WARN) {
                        amount++;
                    }
                }
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(config.getString("display_name"));
            List<String> lore = new ArrayList<>();
            for (String line : config.getStringList("lore")) {
                lore.add(line.replace("%total%", String.valueOf(getTotal())));
            }
            builder.setLore(lore);
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getType() == PunishmentType.WARN) {
                        punishments.add(punishment);
                    }
                }
            }
            new StaffHistoryViewingMenu(profile, punishments, executedPunishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class MutesButton extends Button {
        final Profile profile;
        final Map<UUID, List<Punishment>> executedPunishments;
        final ConfigurationSection config;

        public String getMaterial() {
            return config.getString("material");
        }

        public short getData() {
            return (short) config.getInt("data");
        }

        public int getTotal() {
            int amount = 0;
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getType() == PunishmentType.MUTE) {
                        amount++;
                    }
                }
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(config.getString("display_name"));
            List<String> lore = new ArrayList<>();
            for (String line : config.getStringList("lore")) {
                lore.add(line.replace("%total%", String.valueOf(getTotal())));
            }
            builder.setLore(lore);
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getType() == PunishmentType.MUTE) {
                        punishments.add(punishment);
                    }
                }
            }
            new StaffHistoryViewingMenu(profile, punishments, executedPunishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class PardonsButton extends Button {

        final Profile profile;
        final Map<UUID, List<Punishment>> executedPunishments;
        final ConfigurationSection config;

        public String getMaterial() {
            return config.getString("material");
        }

        public short getData() {
            return (short) config.getInt("data");
        }

        public int getTotal() {
            int amount = 0;
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getAddedBy().equals(profile.getUniqueId())) {
                        amount++;
                    }
                }
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(config.getString("display_name"));
            List<String> lore = new ArrayList<>();
            for (String line : config.getStringList("lore")) {
                lore.add(line.replace("%total%", String.valueOf(getTotal())));
            }
            builder.setLore(lore);
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                for (Punishment punishment : punishmentList) {
                    if (punishment.getRemovedBy() != null && punishment.getRemovedBy().equals(profile.getName())) {
                        punishments.add(punishment);
                    }
                }
            }
            new StaffHistoryViewingMenu(profile, punishments, executedPunishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class AllButton extends Button {

        final Profile profile;
        final Map<UUID, List<Punishment>> executedPunishments;
        final ConfigurationSection config;

        public String getMaterial() {
            return config.getString("material");
        }

        public short getData() {
            return (short) config.getInt("data");
        }

        public int getTotal() {
            int amount = 0;
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                amount += punishmentList.size();
            }
            return amount;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(config.getString("display_name"));

            List<String> lore = new ArrayList<>();
            for (String line : config.getStringList("lore")) {
                lore.add(line.replace("%total%", String.valueOf(getTotal())));
            }

            builder.setLore(lore);
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            List<Punishment> punishments = new ArrayList<>();
            for (List<Punishment> punishmentList : executedPunishments.values()) {
                punishments.addAll(punishmentList);
            }

            new StaffHistoryViewingMenu(profile, punishments, executedPunishments).openMenu(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }
}