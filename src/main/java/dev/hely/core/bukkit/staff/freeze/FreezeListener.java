package dev.hely.core.bukkit.staff.freeze;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.staff.freeze
//   Date: Monday, September 04, 2023 - 7:59 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.commands.implement.staff.FreezeCommand;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.packet.implement.StaffRawMessagePacket;
import dev.hely.lib.CC;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.List;

@RequiredArgsConstructor
public class FreezeListener implements Listener {
    private static final List<String> ALLOWED_COMMANDS = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("frozen-command.settings.allowed-commands");

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile == null || event.isCancelled()) {
            return;
        }

        if (profile.isFrozen() && !FreezeCommand.serverFrozen) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.freeze-chat-player-display").replace("%message%", event.getMessage())));
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.freeze", OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.freeze-chat-player-display").replace("%message%", event.getMessage()).replace("%player%", player.getName())));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (profile.isFrozen()) {
            if (FreezeCommand.serverFrozen) {
                event.getPlayer().sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.join-while-server-frozen")));
                profile.setFrozen(true); // if server is frozen, we want to freeze them, so they cannot reconnect to get unfrozen
                return;
            }
            event.getPlayer().sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.join-frozen")));
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.frozen.alert", OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.join-frozen-alert").replace("%player%", event.getPlayer().getName())));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (profile.isFrozen()) {
            if (FreezeCommand.serverFrozen) {
                profile.setFrozen(false); // we dont want them to join frozen because of server freezeeee
                return;
            }
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.frozen.alert", OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.left-frozen-alert").replace("%player%", event.getPlayer().getName())));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isFrozen(event.getPlayer(), false)) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isFrozen(event.getPlayer(), true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (isFrozen(event.getPlayer(), true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isFrozen(event.getPlayer(), true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();

        for (String allowedCommand : FreezeListener.ALLOWED_COMMANDS) {
            if (message.startsWith(allowedCommand)) {
                return;
            }
        }

        if (isFrozen(event.getPlayer(), true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && isFrozen((Player) event.getEntity(), true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player player = parseDamager(event.getEntity());
        Player damager = parseDamager(event.getDamager());

        if (player == null || damager == null) {
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        if (isFrozen(player, true)) {
            event.setCancelled(true);
            return;
        }

        if (isFrozen(damager, true)) {
            event.setCancelled(true);
        }
    }

    private Player parseDamager(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        }

        if (entity instanceof Projectile && ((Projectile) entity).getShooter() instanceof Player) {
            return (Player) ((Projectile) entity).getShooter();
        }

        return null;
    }


    private boolean isFrozen(Player player, boolean message) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile == null) {
            return false;
        }

        if (profile.isFrozen()) {
            if (message) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.cannot-while-frozen")));
            }
            return true;
        }

        return false;
    }

}
