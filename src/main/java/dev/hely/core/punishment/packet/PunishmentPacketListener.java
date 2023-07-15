package dev.hely.core.punishment.packet;

import com.mongodb.client.model.Filters;
import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.modules.database.redis.packet.PacketListener;
import dev.hely.core.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.profile.Profile;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.core.punishment.broadcast.PunishmentBroadcast;
import dev.hely.core.punishment.packet.implement.PunishmentExecutePacket;
import dev.hely.core.punishment.packet.implement.PunishmentRemovePacket;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 08/07/2023
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class PunishmentPacketListener implements PacketListener {
    
    private Oxygen plugin;

    @IncomingPacketHandler
    public void onPunishmentExecute(PunishmentExecutePacket packet) {
        UUID uniqueId = packet.getUniqueId();
        String sender = packet.getSender();

        Profile profile = plugin.getProfileManager().getProfile(packet.getTarget());

        Punishment punishment = new Punishment();
        punishment.load(plugin.getMongoModule().getPunishmentsCollection().find(Filters.eq("uniqueId", uniqueId.toString())).first());

        profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
        profile.getPunishments().add(punishment);


        String broadcastKey = punishment.getKey(false);
        PunishmentBroadcast punishmentBroadcast = Locale.PUNISHMENT_BROADCAST.get(broadcastKey);

        String broadcast = punishmentBroadcast.getBroadcast()
                .replace("%target%", profile.getName())
                .replace("%sender%", sender)
                .replace("%reason%", punishment.getAddedReason())
                .replace("%duration%", punishment.getRemaining());
        String hover = punishmentBroadcast.getHoverMessage()
                .replace("%target%", profile.getName())
                .replace("%sender%", sender)
                .replace("%reason%", punishment.getAddedReason())
                .replace("%duration%", punishment.getRemaining());

        Bukkit.getConsoleSender().sendMessage(CC.translate(broadcast));

        ComponentBuilder builder = (new ComponentBuilder(CC.translate(broadcast)));

        if (punishment.isSilent()) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!player.hasPermission("oxygen.punishments.silent")) continue;

                if (punishmentBroadcast.isHover()) {
                    if (player.hasPermission("oxygen.punishments.hover")) {
                        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(hover)).create()));
                    }
                }

                player.spigot().sendMessage(builder.create());
            }
        } else {
            for (Player player : plugin.getServer().getOnlinePlayers()) {

                if (punishmentBroadcast.isHover()) {
                    if (player.hasPermission("oxygen.punishments.hover")) {
                        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(hover)).create()));
                    }
                }

                player.spigot().sendMessage(builder.create());
            }
        }


        if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.BLACKLIST || punishment.getType() == PunishmentType.KICK) {
            plugin.getTasks().run(() -> {
                Player player = Bukkit.getPlayer(profile.getUniqueId());
                if (player != null) {
                    String finalMessage = Locale.PUNISHMENT_KICK.get(punishment.getType() == PunishmentType.BAN ?
                            (punishment.isPermanent() ? "ban" : "temp-ban") :
                            punishment.getType() == PunishmentType.BLACKLIST ? "blacklist" : "kick");

                    finalMessage = finalMessage.replace("%target%", profile.getName());
                    finalMessage = finalMessage.replace("%sender%", sender);
                    finalMessage = finalMessage.replace("%reason%", punishment.getAddedReason());
                    finalMessage = finalMessage.replace("%duration%", punishment.getRemaining());
                    finalMessage = finalMessage.replace("%expire%", punishment.getRemaining());

                    player.kickPlayer(CC.translate(finalMessage));
                }
            });
        } else if (punishment.getType() == PunishmentType.WARN || punishment.getType() == PunishmentType.MUTE) {
            Player player = Bukkit.getPlayer(profile.getUniqueId());
            if (player != null) {
                String finalMessage =
                        punishment.getType() == PunishmentType.MUTE ? (punishment.isPermanent() ? Locale.PUNISHMENTS_MUTED_PERMANENT : Locale.PUNISHMENTS_MUTED_TEMP) :
                                (punishment.isPermanent() ? Locale.PUNISHMENTS_WARNED_PERMANENT : Locale.PUNISHMENTS_WARNED_TEMP);

                finalMessage = finalMessage.replace("%sender%", sender);
                finalMessage = finalMessage.replace("%reason%", punishment.getAddedReason());
                finalMessage = finalMessage.replace("%duration%", punishment.getRemaining());
                finalMessage = finalMessage.replace("%expire%", punishment.getRemaining());

                player.sendMessage(CC.translate(finalMessage));
            }
        }
    }

    @IncomingPacketHandler
    public void onPunishmentRemove(PunishmentRemovePacket packet) {
        UUID uniqueId = packet.getUniqueId();
        String sender = packet.getSender();

        Profile profile = plugin.getProfileManager().getProfile(packet.getTarget());

        Punishment punishment = new Punishment();
        punishment.load(plugin.getMongoModule().getPunishmentsCollection().find(Filters.eq("uniqueId", uniqueId.toString())).first());

        profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
        profile.getPunishments().add(punishment);

        String broadcastKey = punishment.getKey(true);
        PunishmentBroadcast punishmentBroadcast = Locale.PUNISHMENT_BROADCAST.get(broadcastKey);

        String broadcast = punishmentBroadcast.getBroadcast()
                .replace("%target%", profile.getName())
                .replace("%sender%", sender)
                .replace("%reason%", punishment.getRemovedReason());

        String hover = punishmentBroadcast.getHoverMessage()
                .replace("%target%", profile.getName())
                .replace("%sender%", sender)
                .replace("%reason%", punishment.getRemovedReason());

        Bukkit.getConsoleSender().sendMessage(CC.translate(broadcast));

        ComponentBuilder builder = (new ComponentBuilder(CC.translate(broadcast)));

        if (punishment.isSilent()) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!player.hasPermission("oxygen.punishments.silent")) continue;

                if (punishmentBroadcast.isHover()) {
                    if (player.hasPermission("oxygen.punishments.hover")) {
                        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(hover)).create()));
                    }
                }

                player.spigot().sendMessage(builder.create());
            }
        } else {
            for (Player player : plugin.getServer().getOnlinePlayers()) {

                if (punishmentBroadcast.isHover()) {
                    if (player.hasPermission("oxygen.punishments.hover")) {
                        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(hover)).create()));
                    }
                }

                player.spigot().sendMessage(builder.create());
            }
        }

    }
}
