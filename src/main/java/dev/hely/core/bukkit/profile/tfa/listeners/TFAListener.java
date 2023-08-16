package dev.hely.core.bukkit.profile.tfa.listeners;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.tfa.listeners
//   Date: Thursday, August 10, 2023 - 5:04 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.tfa.utils.TFAUtil;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.IOException;

@Getter
public class TFAListener implements Listener {
    private boolean isNeeding2FA(Player player) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        return profile != null && (profile.isNeedsVerify() || profile.isSettingUpAuth()) && player.hasPermission("oxygen.staff");
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        if (command.startsWith("/auth") || command.startsWith("/2fa")) {
            return;
        }
        if (isNeeding2FA(player)) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou need to authenticate your account before continue."));
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand().replace("/", "");

        if (command == null || command.split(" ").length <= 0) {
            return;
        }

        if(event.getSender() instanceof Player) {
            Player player = (Player) event.getSender();
            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (profile == null) {
                return;
            }
            if (isNeeding2FA(player)) {
                return;
            }
        }
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(event.getUniqueId());
        if (profile == null) {
            return;
        }
        if (profile.getCurrentAddress().equals("") || profile.getNextAuthenticationNeeded() == -1) {
            if (OxygenBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("two-factor.settings.require-op")) {
                if (Bukkit.getPlayer(event.getUniqueId()).isOp()) {
                    profile.setNeedsVerify(true);
                }
            }
            if (profile.getPlayerPermissions().contains(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("two-factor.settings.permission")) || profile.getBungeePermissions().contains(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("two-factor.settings.permission"))) {
                profile.setSettingUpAuth(true);
            }
        } else {
            if (System.currentTimeMillis() >= profile.getNextAuthenticationNeeded() || !profile.getCurrentAddress().equals(event.getAddress().getHostAddress())) {
                profile.setNeedsVerify(true);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());

        if (!profile.isNeedsVerify() && profile.getNextAuthenticationNeeded() != -1) {
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.next-auth-needed")) {
                player.sendMessage(CC.translate(message).replace("%time%", TimeUtil.getMoreDetailedTime(profile.getNextAuthenticationNeeded() - System.currentTimeMillis())));
            }
        } else if (profile.isNeedsVerify() || profile.getNextAuthenticationNeeded() == -1) {
            OxygenBukkit.getInstance().getServer().getScheduler().runTaskLater(OxygenBukkit.getInstance(), () -> {
                if (OxygenBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("two-factor.settings.join-sound.enabled")) {
                    Sound join_sound = Sound.valueOf(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("two-factor.settings.join-sound.name"));

                    player.playSound(player.getLocation(), join_sound, 5, 5);
                }

                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.join")) {
                    player.sendMessage(CC.translate(message));
                }
                OxygenBukkit.getInstance().getTfaManager().giveQRMap(player);
            }, 20);
        }
    }

    @EventHandler
    public void PlayerItemHeldEvent(PlayerItemHeldEvent e) {
        if (isNeeding2FA(e.getPlayer())) { // Stop players from moving cursor
            if (e.getNewSlot() == 0 || e.getNewSlot() == 1 || e.getNewSlot() == 2 || e.getNewSlot() == 3 || e.getNewSlot() == 5 || e.getNewSlot() == 6 || e.getNewSlot() == 7 || e.getNewSlot() == 8 && e.getPreviousSlot() == 4) {
                e.setCancelled(true);
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(e.getPlayer().getUniqueId());
                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.next-auth-needed")) {
                    e.getPlayer().sendMessage(CC.translate(message).replace("%time%", TimeUtil.getMoreDetailedTime(profile.getNextAuthenticationNeeded() - System.currentTimeMillis())));
                }
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        TFAUtil.removeQrMapFromInventory(player);
        OxygenBukkit.getInstance().getTfaManager().giveItems(player);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (profile == null) return;

        if (isNeeding2FA(event.getPlayer())) {
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                event.getPlayer().sendMessage(CC.translate(message));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isNeeding2FA(event.getPlayer())) {
            event.setCancelled(true);
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                event.getPlayer().sendMessage(CC.translate(message));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player)event.getPlayer();
        if (isNeeding2FA(player)) {
            event.setCancelled(true);
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                event.getPlayer().sendMessage(CC.translate(message));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (isNeeding2FA(event.getPlayer())) {
            event.setCancelled(true);
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                event.getPlayer().sendMessage(CC.translate(message));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (isNeeding2FA(event.getPlayer())) {
            event.setCancelled(true);
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                event.getPlayer().sendMessage(CC.translate(message));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player)event.getDamager();
            if (isNeeding2FA(player)) {
                event.setCancelled(true);
                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                    player.sendMessage(CC.translate(message));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isNeeding2FA(player)) {
            event.setCancelled(true);
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                event.getPlayer().sendMessage(CC.translate(message));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if (isNeeding2FA(player)) {
            event.setCancelled(true);
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                event.getPlayer().sendMessage(CC.translate(message));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        if (isNeeding2FA(player)) {
            event.setCancelled(true);
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                event.getPlayer().sendMessage(CC.translate(message));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (isNeeding2FA(player)) {
            event.setCancelled(true);
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                player.sendMessage(CC.translate(message));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location to = event.getTo();
        Location from = event.getFrom();
        if (isNeeding2FA(player)) {
            if ((from.getX() != to.getX()) || (from.getZ() != to.getZ())) {
                player.teleport(from);
                for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("two-factor.need-authenticate")) {
                    event.getPlayer().sendMessage(CC.translate(message));
                }
            }
        }
    }
}
