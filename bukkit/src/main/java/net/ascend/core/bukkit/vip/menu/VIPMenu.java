package net.ascend.core.bukkit.vip.menu;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.types.RankGrant;
import net.ascend.core.bukkit.util.NewColorsUtil;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import net.ascend.core.bukkit.lib.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
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
        return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public int getSize() {
        return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.size") * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.status-button.slot"), new Status_Button(profile));
        buttons.put(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.color-button.slot"), new Color_Button(profile));
        buttons.put(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.rewards-button.slot"), new Rewards_Button(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class Status_Button extends Button {
        private final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.status-button.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.status-button.data");
        }

        public boolean isUseSkull() {
            return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getBoolean("menu.buttons.status-button.use-skull");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            boolean vip_status = false;
            RankGrant vipGrant = null;
            for (RankGrant grant : profile.getRankGrants()) {
                if (grant.getRankName().equalsIgnoreCase(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.settings.grant-name"))) {
                    vip_status = true;
                    vipGrant = grant;
                }
            }

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            if (isUseSkull()) {
                builder.setOwner(player.getName());
            }
            builder.setDisplayName(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.status-button.display-name"));
            for (String line : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("menu.buttons.status-button.lore")) {
                builder.addLore(line
                        .replace("%status%", CC.colorBoolean(profile.hasVIPStatus(), "Yes", "No"))
                        .replace("%remaining%", getDuration(vipGrant))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }

        public String getDuration(RankGrant grant) {
            if (grant == null) {
                return "None";
            } else {
                if (grant.getDuration() == -1) {
                    return "Permanent";
                } else {
                    grant.getRemaining();
                }
            }
            return "None";
        }
    }

    @AllArgsConstructor
    public static class Color_Button extends Button {
        private final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.color-button.material");
        }

        public short getData() {
            if (isUseSelectedColorData()) {
                return NewColorsUtil.COLOR_MAP.get(ChatColor.valueOf(profile.getVipColor())).getWoolData();
            } else {
                return (short) XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.color-button.data");
            }
        }

        public boolean isUseSelectedColorData() {
            return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getBoolean("menu.buttons.color-button.selected-color-data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.color-button.display-name"));
            for (String line : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("menu.buttons.color-button.lore")) {
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
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.no-vip-status")));
            }
        }
    }

    @AllArgsConstructor
    public static class Rewards_Button extends Button {
        private final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.rewards-button.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.buttons.rewards-button.data");
        }

        public int getVipRewardsAmount() {
            return XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getConfigurationSection("rewards").getKeys(false).size();
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.buttons.rewards-button.display-name"));
            for (String string : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("menu.buttons.rewards-button.lore")) {
                builder.addLore(string.replace("%time%", TimeUtil.formatTimeMillis(profile.getVipCooldown() - System.currentTimeMillis())).replace("%amount%", String.valueOf(getVipRewardsAmount())));
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (!profile.hasVIPStatus()) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.no-vip-status")));
                    return;
                }
                if (getVipRewardsAmount() == 0) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.no-rewards")));
                    return;
                }
                if (profile.getVipCooldown() > System.currentTimeMillis()) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.cooldown").replace("%time%", TimeUtil.formatTimeMillis(profile.getVipCooldown() - System.currentTimeMillis()))));
                    return;
                }
                profile.setVipCooldown(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getInt("menu.settings.day-cooldown")));
                profile.save();

                player.closeInventory();
                for (String key : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getConfigurationSection("rewards").getKeys(false)) {
                    for (String command : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("rewards." + key + ".commands")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                    }
                }
                for (String broadcast_lines : XenonBukkit.getInstance().getVipMenuConfig().getConfiguration().getStringList("menu.messages.broadcast")) {
                    Bukkit.broadcastMessage(CC.translate(broadcast_lines).replace("%player%", player.getName()));
                }
            } else {
                new VIPRewardsMenu(profile).openMenu(player);
            }
        }
    }
}
