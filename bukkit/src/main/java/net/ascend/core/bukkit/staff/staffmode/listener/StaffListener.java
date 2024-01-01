package net.ascend.core.bukkit.staff.staffmode.listener;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.staff.staffmode.listeners
//   Date: Saturday, August 26, 2023 - 4:36 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.staff.staffmode.data.StaffHotbar;
import net.ascend.core.bukkit.staff.staffmode.menus.InventoryMenu;
import net.ascend.core.bukkit.staff.staffmode.menus.StaffMenu;
import net.ascend.core.bukkit.util.PlayerUtil;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class StaffListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("xenon.command.vanish")) {
            XenonBukkit.getInstance().getStaffManager().getVanishedPlayers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(player::hidePlayer);
            return;
        }
        if (player.hasPermission("xenon.command.staff")) {
            if (XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff-mode.on_join.staff_mode")) {
                XenonBukkit.getInstance().getStaffManager().enableStaffMode(player);
                return;
            }
            if (XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("staff-mode.on_join.vanish")) {
                XenonBukkit.getInstance().getStaffManager().enabledVanish(player);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY && event.useItemInHand() == Event.Result.DENY) return;
        Player player = event.getPlayer();

        if (!XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player)) return;

        ItemStack itemStack = event.getItem();

        if (!event.hasItem() || !itemStack.hasItemMeta()) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        StaffHotbar randomTeleport = XenonBukkit.getInstance().getStaffManager().getHotbarData().get(4);
        StaffHotbar staffGui = XenonBukkit.getInstance().getStaffManager().getHotbarData().get(5);
        StaffHotbar vanishOff = XenonBukkit.getInstance().getStaffManager().getHotbarData().get(6);
        StaffHotbar vanishOn = XenonBukkit.getInstance().getStaffManager().getHotbarData().get(7);


        if (itemStack.isSimilar(staffGui.getItemStack())) {
            event.setCancelled(true);
            new StaffMenu().openMenu(player);
        } else if (itemStack.isSimilar(randomTeleport.getItemStack())) {
            event.setCancelled(true);

            List<Player> online = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(other -> {
                if (!other.equals(player) && !XenonBukkit.getInstance().getStaffManager().isVanished(other) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(other) || !other.hasPermission("*") || !other.isOp()) {
                    online.add(other);
                }
            });

            if (online.isEmpty()) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-mode-system.no-online-players")));
                return;
            }
            Player other;

            if (online.size() == 1) other = online.get(0);
            else other = online.get(ThreadLocalRandom.current().nextInt(online.size()));
            player.performCommand("tp " + other.getName());
        } else if (itemStack.isSimilar(vanishOn.getItemStack())) {
            event.setCancelled(true);
            XenonBukkit.getInstance().getStaffManager().disabledVanish(player);
            player.getInventory().setItem(vanishOff.getSlot() - 1, vanishOff.getItemStack());
            player.updateInventory();
        } else if (itemStack.isSimilar(vanishOff.getItemStack())) {
            event.setCancelled(true);
            XenonBukkit.getInstance().getStaffManager().enabledVanish(player);
            player.getInventory().setItem(vanishOn.getSlot() - 1, vanishOn.getItemStack());
            player.updateInventory();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!XenonBukkit.getInstance().getStaffManager().isVanished(event.getPlayer()) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(event.getPlayer()))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(event.getPlayer())) return;

        Action action = event.getAction();

        if (action == Action.PHYSICAL) {
            event.setCancelled(true);
            return;
        }

        if (action != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) && !player.isSneaking()) {
            event.setCancelled(true);
            Chest chest = (Chest) block.getState();

            Inventory inventory = Bukkit.createInventory(null, chest.getInventory().getSize(), CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.silent_view_title")));
            inventory.setContents(chest.getInventory().getContents());

            player.openInventory(inventory);
            return;
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (!XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player)) return;

        ItemStack itemStack = event.getPlayer().getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        if (!itemStack.hasItemMeta()) return;

        Entity entity = event.getRightClicked();
        if (!(entity instanceof Player)) return;

        Player target = (Player) entity;

        StaffHotbar inspector = XenonBukkit.getInstance().getStaffManager().getHotbarData().get(1);
        StaffHotbar freeze = XenonBukkit.getInstance().getStaffManager().getHotbarData().get(2);


        if (itemStack.isSimilar(inspector.getItemStack())) {
            event.setCancelled(true);
            new InventoryMenu(target).openMenu(player);
        } else if (itemStack.isSimilar(freeze.getItemStack())) {
            event.setCancelled(true);
            player.performCommand("freeze " + target.getName());

        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!XenonBukkit.getInstance().getStaffManager().isVanished((Player) event.getEntity()) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive((Player) event.getEntity()))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild((Player) event.getEntity())) return;

        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) return;
        if (!XenonBukkit.getInstance().getStaffManager().isVanished((Player) event.getTarget()) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive((Player) event.getTarget()))
            return;

        event.setCancelled(true);
    }


    @EventHandler(ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!XenonBukkit.getInstance().getStaffManager().isVanished(player) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) {
            for (StaffHotbar hotbar : XenonBukkit.getInstance().getStaffManager().getHotbarData()) {
                if (hotbar.getItemStack().isSimilar(event.getPlayer().getItemInHand())) {
                    return;
                }
            }
        }

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    private void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!XenonBukkit.getInstance().getStaffManager().isVanished(player) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!XenonBukkit.getInstance().getStaffManager().isVanished(player) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player))
            return;

        event.getDrops().clear();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!XenonBukkit.getInstance().getStaffManager().isStaffModeActive((Player) event.getEntity()) || !XenonBukkit.getInstance().getStaffManager().isVanished((Player) event.getEntity()))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild((Player) event.getEntity())) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Player attacker = PlayerUtil.getAttacker(event, false);
        if (attacker == null) return;

        if (!XenonBukkit.getInstance().getStaffManager().isStaffModeActive(attacker) || !XenonBukkit.getInstance().getStaffManager().isVanished(attacker))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(attacker)) return;


        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!XenonBukkit.getInstance().getStaffManager().isVanished(player) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!XenonBukkit.getInstance().getStaffManager().isVanished(player) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player))
            return;

        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) return;

        event.setCancelled(true);

        Block block = event.getBlock();
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (XenonBukkit.getInstance().getStaffManager().isStaffModeActive(event.getPlayer())) {
            XenonBukkit.getInstance().getStaffManager().disableStaffMode(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        if (!XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player)) return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void handleSilentChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!XenonBukkit.getInstance().getStaffManager().isVanished(player) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) return;

        Block block = event.getClickedBlock();

        if (block == null) return;
        if (block.getType() != Material.CHEST && block.getType() != Material.TRAPPED_CHEST) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        event.setCancelled(true);

        if (player.isSneaking()) return;

        Chest chest = (Chest) block.getState();


        Inventory inventory = Bukkit.createInventory(null, chest.getInventory().getSize(), CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.silent_view_title")));
        inventory.setContents(chest.getInventory().getContents());

        player.openInventory(inventory);
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-mode-system.open-chest")));
    }

    @EventHandler
    public void handleSilentFurnance(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!XenonBukkit.getInstance().getStaffManager().isVanished(player) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) return;


        Block block = event.getClickedBlock();

        if (block == null) return;
        if (block.getType() != Material.FURNACE) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        event.setCancelled(true);

        if (player.isSneaking()) return;

        Furnace furnace = (Furnace) block.getState();
        double percentage = ((furnace.getCookTime() / 20.0D)) * 100;
        String fuelLeft = TimeUtil.formatTimeMillis(furnace.getBurnTime() / 20);

        ItemStack item = ItemMaker.of(furnace.getInventory().getResult().getType())
                .setAmount(furnace.getInventory().getResult().getAmount())
                .setData(furnace.getInventory().getResult().getData().getData())
                .setLore(Arrays.asList(
                        "&6Amount Smelted&7: &b" + furnace.getInventory().getResult().getAmount(),
                        "&6Progress&7: &b" + (percentage > 100 ? 100 : Math.round(percentage)) + "&b%",
                        "&6Fuel Remaining&7: &b" + fuelLeft
                ))
                .build();

        Inventory inventory = Bukkit.createInventory(null, furnace.getInventory().getType(), CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.silent_view_title")));
        furnace.getInventory().setResult(item);
        inventory.setContents(furnace.getInventory().getContents());


        player.openInventory(inventory);
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-mode-system.open-furnace")));
    }

    @EventHandler
    public void handleSilentHoppers(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!XenonBukkit.getInstance().getStaffManager().isVanished(player) || !XenonBukkit.getInstance().getStaffManager().isStaffModeActive(player))
            return;
        if (XenonBukkit.getInstance().getStaffManager().isStaffBuild(player)) return;


        Block block = event.getClickedBlock();

        if (block == null) return;
        if (block.getType() != Material.HOPPER) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        event.setCancelled(true);

        if (player.isSneaking()) return;

        Hopper hopper = (Hopper) block.getState();


        Inventory inventory = Bukkit.createInventory(null, hopper.getInventory().getType(), CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.silent_view_title")));
        inventory.setContents(hopper.getInventory().getContents());

        player.openInventory(inventory);
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-mode-system.open-hopper")));
    }
}
