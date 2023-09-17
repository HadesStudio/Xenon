package dev.hely.core.bukkit.vip.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.vip.menu
//   Date: Saturday, September 16, 2023 - 3:11 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.utils.NewColorsUtil;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.fill.FillerTemplate;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@AllArgsConstructor
public class VIPMenu extends Menu {
    Profile profile;

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.size") * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.status-button.slot"), new Status_Button(profile));
        buttons.put(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.color-button.slot"), new Color_Button(profile));
        buttons.put(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.rewards-button.slot"), new Rewards_Button(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class Status_Button extends Button {
        private final Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.status-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.status-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.status-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'vip-status-button' is null, using a default material."));
                    return Material.SKULL_ITEM;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'vip-status-button' is broken, using a default material."));
                return Material.SKULL_ITEM;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.status-button.data");
        }

        public boolean isUseSkull() {
            return OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getBoolean("menu.buttons.status-button.use-skull");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            Optional<Grant> vip_status = Optional.empty();
            for (Grant grant : profile.getActiveGrants()) {
                if (grant.getRank().getName().equalsIgnoreCase(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.settings.grant-name"))) {
                    vip_status = Optional.of(grant);
                }
            }

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            if (isUseSkull()) {
                builder.setOwner(player.getName());
            }
            builder.setDisplayName(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.status-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("menu.buttons.status-button.lore")) {
                builder.addLore(line
                        .replace("%status%", CC.colorBoolean(profile.hasVIPStatus(), "Yes", "No"))
                        .replace("%remaining%", vip_status.map(grant -> grant.getDuration() == -1 ? "Permanent" : grant.getRemaining()).orElse("None"))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }

    @AllArgsConstructor
    public static class Color_Button extends Button {
        private final Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.color-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.color-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.color-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'vip-color-button' is null, using a default material."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'vip-color-button' is broken, using a default material."));
                return Material.WOOL;
            }
        }

        public short getData() {
            if (isUseSelectedColorData()) {
                return NewColorsUtil.COLOR_MAP.get(ChatColor.valueOf(profile.getVipColor())).getWoolData();
            } else {
                return (short) OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.color-button.data");
            }
        }

        public boolean isUseSelectedColorData() {
            return OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getBoolean("menu.buttons.color-button.selected-color-data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.color-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("menu.buttons.color-button.lore")) {
                builder.addLore(line
                        .replace("%color%", profile.getVipColor())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (profile.hasVIPStatus()) {
                new VIPColorMenu(profile).openMenu(player);
            } else {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.no-vip-status")));
            }
        }
    }

    @AllArgsConstructor
    public static class Rewards_Button extends Button {
        private final Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.rewards-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.rewards-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.rewards-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'vip-rewards-button' is null, using a default material."));
                    return Material.TRIPWIRE_HOOK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'vip-rewards-button' is broken, using a default material."));
                return Material.TRIPWIRE_HOOK;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.rewards-button.data");
        }


        public int getVipRewardsAmount() {
            return OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getConfigurationSection("rewards").getKeys(false).size();
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.rewards-button.display-name"));
            for (String string : OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("menu.buttons.rewards-button.lore")) {
                builder.addLore(string.replace("%time%", TimeUtil.formatTimeMillis(profile.getVipCooldown() - System.currentTimeMillis())).replace("%amount%", String.valueOf(getVipRewardsAmount())));
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (!profile.hasVIPStatus()) {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.no-vip-status")));
                    return;
                }
                if (getVipRewardsAmount() == 0) {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.no-rewards")));
                    return;
                }
                if (profile.getVipCooldown() > System.currentTimeMillis()) {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.cooldown").replace("%time%", TimeUtil.formatTimeMillis(profile.getVipCooldown() - System.currentTimeMillis()))));
                    return;
                }
                profile.setVipCooldown(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.settings.day-cooldown")));
                profile.save();

                player.closeInventory();
                for (String key : OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getConfigurationSection("rewards").getKeys(false)) {
                    for (String command : OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("rewards." + key + ".commands")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                    }
                }
                for (String broadcast_lines : OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("menu.messages.broadcast")) {
                    Bukkit.broadcastMessage(CC.translate(broadcast_lines).replace("%player%", player.getName()));
                }
            } else {
                new VIPRewardsMenu(profile).openMenu(player);
            }
        }
    }
}
