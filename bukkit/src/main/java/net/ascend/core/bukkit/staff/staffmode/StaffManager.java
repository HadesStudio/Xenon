package net.ascend.core.bukkit.staff.staffmode;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.staff.staffmode
//   Date: Saturday, August 26, 2023 - 3:35 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.commands.implement.staff.HideStaffCommand;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.staff.staffmode.data.StaffArmor;
import net.ascend.core.bukkit.staff.staffmode.data.StaffHotbar;
import net.ascend.core.bukkit.staff.staffmode.data.StaffPlayerData;
import net.ascend.core.bukkit.staff.staffmode.event.StaffModeToggleEvent;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class StaffManager implements ModuleLoader {

    private boolean enabled;

    private Map<UUID, StaffPlayerData> staffMode;
    private HashSet<UUID> staffToggled, vanishedPlayers, staffBuild;
    private List<StaffArmor> armors;
    private List<StaffHotbar> hotbarData;

    @Override
    public void onEnable() {
        staffMode = new HashMap<>();
        vanishedPlayers = new HashSet<>();
        staffBuild = new HashSet<>();
        staffToggled = new HashSet<>();
        hotbarData = new ArrayList<>();
        armors = new ArrayList<>();
        enabled = true;

        setupHotbarData();
    }

    @Override
    public void onDisable() {
        enabled = false;
    }

    @Override
    public void onReload() {

    }

    public void setupHotbarData() {
        hotbarData.clear();
        ItemStack item;
        int slot;
        boolean enabled;

        enabled = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.hotbar.teleportation.enabled");
        item = ItemMaker.of(XenonBukkit.getInstance().getConfiguration().getConfig().getString("staff_mode.hotbar.teleportation.item.material"))
                .setData((short) XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.teleportation.item.data"))
                .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.teleportation.item.name"))
                .setLore(XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.hotbar.teleportation.item.lore"))
                .build();

        slot = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.teleportation.slot");
        hotbarData.add(new StaffHotbar(item, slot, enabled));

        enabled = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.hotbar.inspection.enabled");
        item = ItemMaker.of(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.inspection.item.material"))
                .setData((short) XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.inspection.item.data"))
                .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.inspection.item.name"))
                .setLore(XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.hotbar.inspection.item.lore"))
                .build();

        slot = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.inspection.slot");
        hotbarData.add(new StaffHotbar(item, slot, enabled));

        enabled = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.hotbar.freeze_player.enabled");
        item = ItemMaker.of(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.freeze_player.item.material"))
                .setData((short) XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.freeze_player.item.data"))
                .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.freeze_player.item.name"))
                .setLore(XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.hotbar.freeze_player.item.lore"))
                .build();

        slot = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.freeze_player.slot");
        hotbarData.add(new StaffHotbar(item, slot, enabled));

        enabled = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.hotbar.worldedit_wand.enabled");
        item = ItemMaker.of(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.worldedit_wand.item.material"))
                .setData((short) XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.worldedit_wand.item.data"))
                .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.worldedit_wand.item.name"))
                .setLore(XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.hotbar.worldedit_wand.item.lore"))
                .build();

        slot = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.worldedit_wand.slot");
        hotbarData.add(new StaffHotbar(item, slot, enabled));

        enabled = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.hotbar.random_teleportation.enabled");
        item = ItemMaker.of(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.random_teleportation.item.material"))
                .setData((short) XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.random_teleportation.item.data"))
                .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.random_teleportation.item.name"))
                .setLore(XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.hotbar.random_teleportation.item.lore"))
                .build();

        slot = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.random_teleportation.slot");
        hotbarData.add(new StaffHotbar(item, slot, enabled));

        enabled = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.hotbar.online_staff.enabled");
        item = ItemMaker.of(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.online_staff.item.material"))
                .setData((short) XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.online_staff.item.data"))
                .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.online_staff.item.name"))
                .setLore(XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.hotbar.online_staff.item.lore"))
                .build();

        slot = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.online_staff.slot");
        hotbarData.add(new StaffHotbar(item, slot, enabled));

        enabled = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.hotbar.vanish.enabled_button.enabled");
        item = ItemMaker.of(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.vanish.enabled_button.item.material"))
                .setData((short) XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.vanish.enabled_button.item.data"))
                .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.vanish.enabled_button.item.name"))
                .setLore(XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.hotbar.vanish.enabled_button.item.lore"))
                .build();

        slot = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.vanish.enabled_button.slot");
        hotbarData.add(new StaffHotbar(item, slot, enabled));

        enabled = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff_mode.hotbar.vanish.disabled_button.enabled");
        item = ItemMaker.of(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.vanish.disabled_button.item.material"))
                .setData((short) XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.vanish.disabled_button.item.data"))
                .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.hotbar.vanish.disabled_button.item.name"))
                .setLore(XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.hotbar.vanish.disabled_button.item.lore"))
                .build();

        slot = XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("staff_mode.hotbar.vanish.disabled_button.slot");
        hotbarData.add(new StaffHotbar(item, slot, enabled));
    }

    public void enableStaffMode(Player player) {
        StaffPlayerData staffData = new StaffPlayerData();
        staffData.setContents(player.getInventory().getContents());
        staffData.setArmor(player.getInventory().getArmorContents());
        staffData.setGameMode(player.getGameMode());

        staffMode.put(player.getUniqueId(), staffData);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setGameMode(GameMode.CREATIVE);

        boolean vanished = isVanished(player);

        StaffHotbar vanishOn = hotbarData.get(6);
        StaffHotbar vanishOff = hotbarData.get(7);


        hotbarData.forEach(hotbar -> {
            if (!hotbar.isEnabled()) return;
            if (vanished && hotbar == vanishOn) return;
            if (!vanished && hotbar == vanishOff) return;


            player.getInventory().setItem(hotbar.getSlot() - 1, hotbar.getItemStack());
        });
        enabledVanish(player);
        player.updateInventory();

        new StaffModeToggleEvent(player, true);
    }

    public void disableStaffMode(Player player) {
        disabledVanish(player);

        StaffPlayerData staffData = staffMode.get(player.getUniqueId());
        player.getInventory().setContents(staffData.getContents());
        player.getInventory().setArmorContents(staffData.getArmor());
        player.setGameMode(staffData.getGameMode());

        player.updateInventory();

        staffMode.remove(player.getUniqueId());

        new StaffModeToggleEvent(player, false);
    }


    public void enabledVanish(Player player) {
        vanishedPlayers.add(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.hasPermission("hcf.command.staff") || HideStaffCommand.hidden.contains(online.getUniqueId())) {
                online.hidePlayer(player);
            }
        }

        //TODO:
        //player.spigot().setCollidesWithEntities(false);

        if (XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getBoolean("vanish.enable-alert.enabled")) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.hasPermission("xenon.staff.alerts")) {
                    XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("vanish.enable-alert.alert").forEach(line -> online.sendMessage(CC.translate(line).replace("%player%", player.getName())));
                }
            }
        }
    }

    public void disabledVanish(Player player) {
        vanishedPlayers.remove(player.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!online.canSee(player)) online.showPlayer(player);
        });

        //TODO:
        //player.spigot().setCollidesWithEntities(true);

        if (XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getBoolean("vanish.disable-alert.enabled")) {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (online.hasPermission("xenon.staff.alerts")) {
                    XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("vanish.disable-alert.alert").forEach(line -> online.sendMessage(CC.translate(line).replace("%player%", player.getName())));
                }
            }
        }
    }

    public boolean isStaffBuild(Player player) {
        return this.staffBuild.contains(player.getUniqueId());
    }

    public void enableStaffBuild(Player player) {
        this.staffBuild.add(player.getUniqueId());
    }

    public void disableStaffBuild(Player player) {
        this.staffBuild.remove(player.getUniqueId());
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }

    public boolean isStaffModeActive(Player player) {
        return staffMode.containsKey(player.getUniqueId());
    }
}

