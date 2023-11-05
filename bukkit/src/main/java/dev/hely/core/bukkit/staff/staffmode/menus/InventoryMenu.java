package dev.hely.core.bukkit.staff.staffmode.menus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.staff.staffmode.menus
//   Date: Saturday, August 26, 2023 - 9:15 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.BukkitUtils;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.material.MaterialUtil;
import dev.hely.lib.util.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class InventoryMenu extends Menu {
    private Player target;
    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.title").replace("%player_name%", target.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.title").replace("%player_name%", target.getName());
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (OxygenBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.inventory_view.fill.enabled")) {
            for (int i = 0; i < getSize(); ++i) {
                buttons.put(i, new SeparatorButton());
            }
        }

        ItemMaker health = ItemMaker.of(Material.getMaterial(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.buttons.health.item.material")));
        health.setData((short) OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.health.item.data"));
        health.setDisplayName(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.buttons.health.item.name"));
        for (String healhLine : OxygenBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.inventory_view.buttons.health.item.lore")) {
            health.addLore(CC.translate(healhLine.replace("%health%", String.valueOf((int) this.target.getHealth()))));
        }

        buttons.put(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.health.slot") - 1, new ButtonBuilder(health.build()));


        ItemMaker food = ItemMaker.of(Material.getMaterial(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.buttons.food.item.material")));
        food.setData((short) OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.food.item.data"));
        food.setDisplayName(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.buttons.food.item.name"));
        for (String healhLine : OxygenBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.inventory_view.buttons.food.item.lore")) {
            food.addLore(CC.translate(healhLine
                    .replace("%saturation%", String.valueOf((int) this.target.getSaturation()))
                    .replace("%food_level%", String.valueOf(this.target.getFoodLevel()))));
        }

        buttons.put(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.food.slot") - 1, new ButtonBuilder(food.build()));


        ItemMaker effects = ItemMaker.of(Material.getMaterial(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.buttons.active_effects.item.material")));
        effects.setData((short) OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.active_effects.item.data"));
        effects.setDisplayName(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.buttons.active_effects.item.name"));
        target.getActivePotionEffects().forEach(effect ->
                effects.addLore(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.buttons.active_effects.item.lore")
                        .replace("%effect_name%", BukkitUtils.getPotionEffectName(effect))
                        .replace("%effect_level%", effect.getAmplifier() + 1 + "")
                        .replace("%duration%", String.valueOf(effect.getDuration() / 20))));

        buttons.put(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.active_effects.slot") - 1, new ButtonBuilder(effects.build()));


        ItemMaker playerInfo = ItemMaker.of(MaterialUtil.HUMAN_SKULL.getMaterial(), 1, 3);
        playerInfo.setOwner(this.target.getName());
        playerInfo.setDisplayName(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.inventory_view.buttons.player_info.item.name"));

        Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId());
        for (String healhLine : OxygenBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.inventory_view.buttons.player_info.item.lore")) {
            playerInfo.addLore(CC.translate(healhLine
                    .replace("%rank_prefix%", targetProfile.getActiveRank().getPrefix())
                    .replace("%rank_name%", targetProfile.getActiveRank().getName())
                    .replace("%playtime%", TimeUtil.getMoreDetailedTime(targetProfile.getTotalPlayTime()))
            ));
        }

        buttons.put(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.player_info.slot") - 1, new ButtonBuilder(playerInfo.build()));

        for (int slot = 0; slot < this.target.getInventory().getSize(); ++slot) {
            if (this.target.getInventory().getItem(slot) != null) {
                buttons.put(slot, new ButtonBuilder(this.target.getInventory().getItem(slot)));
            }
        }

        if (this.target.getInventory().getHelmet() != null) {
            buttons.put(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.helmet_slot") - 1, new ButtonBuilder(this.target.getInventory().getHelmet()));
        }
        if (this.target.getInventory().getChestplate() != null) {
            buttons.put(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.chestplate_slot") - 1, new ButtonBuilder(this.target.getInventory().getChestplate()));
        }
        if (this.target.getInventory().getLeggings() != null) {
            buttons.put(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.leggings_slot") - 1, new ButtonBuilder(this.target.getInventory().getLeggings()));
        }
        if (this.target.getInventory().getBoots() != null) {
            buttons.put(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.buttons.boots_slot") - 1, new ButtonBuilder(this.target.getInventory().getBoots()));
        }

        return buttons;
    }

    private static class SeparatorButton extends Button {

        @Override
        public ItemStack getItemStack(Player player) {
            return ItemMaker.of(Material.valueOf("STAINED_GLASS_PANE")).setDisplayName("&8")
                    .setData((short) OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.inventory_view.fill.data")).build();
        }
    }


    @AllArgsConstructor
    private static class ButtonBuilder extends Button {

        private final ItemStack itemStack;

        @Override
        public ItemStack getItemStack(Player player) {
            return itemStack;
        }
    }
}

