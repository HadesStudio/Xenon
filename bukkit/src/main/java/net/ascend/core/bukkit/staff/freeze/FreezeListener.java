package net.ascend.core.bukkit.staff.freeze;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.commands.implement.staff.FreezeCommand;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.packet.implement.StaffRawMessagePacket;
import net.ascend.lib.CC;
import lombok.RequiredArgsConstructor;
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
    private static final List<String> ALLOWED_COMMANDS = XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("frozen-command.settings.allowed-commands");

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile == null || event.isCancelled()) {
            return;
        }

        if (isFrozen(player, false)) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.freeze-chat-player-display").replace("%message%", event.getMessage())));
            XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.freeze", XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.freeze-chat-player-display").replace("%message%", event.getMessage()).replace("%player%", player.getName())));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (isFrozen(event.getPlayer(), false)) {
            if (FreezeCommand.serverFrozen) {
                event.getPlayer().sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.join-while-server-frozen")));
                profile.setFrozen(true); // if server is frozen, we want to freeze them, so they cannot reconnect to get unfrozen
                profile.save();
                return;
            }
            event.getPlayer().sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.join-frozen")));
            XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.frozen.alert", XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.join-frozen-alert").replace("%player%", event.getPlayer().getName())));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (profile.isFrozen()) {
            if (FreezeCommand.serverFrozen) {
                profile.setFrozen(false); // we dont want them to join frozen because of server freezeeee
                profile.save();
                return;
            }
            XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.frozen.alert", XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.left-frozen-alert").replace("%player%", event.getPlayer().getName())));
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

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
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
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile == null) {
            return false;
        }

        if (profile.isFrozen()) {
            if (message) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.messages.cannot-while-frozen")));
            }
            return true;
        }

        return false;
    }

}
