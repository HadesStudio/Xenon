package dev.hely.core.bukkit.punishment.packet;

import com.mongodb.client.model.Filters;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.punishment.packet.implement.PunishmentAttemptedJoinPacket;
import dev.hely.core.bukkit.punishment.packet.implement.PunishmentRemovePacket;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.punishment.PunishmentType;
import dev.hely.core.bukkit.punishment.broadcast.PunishmentBroadcast;
import dev.hely.core.bukkit.punishment.packet.implement.PunishmentExecutePacket;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 08/07/2023
 * Protect Name OxygenGlobal
 */
@AllArgsConstructor
public class PunishmentPacketListener implements PacketListener {
    
    private OxygenBukkit plugin;

    @IncomingPacketHandler
    public void onPunishmentJoin(PunishmentAttemptedJoinPacket packet) {
        UUID uniqueId = packet.getUniqueId();
        PunishmentType type = packet.getType();

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(uniqueId);

        if (type == PunishmentType.BLACKLIST) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!player.hasPermission("oxygen.punishments.silent")) continue;

                List<String> hoverMsg = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("punishments.broadcast.blacklist.attempted-join.hover");
                String broadcastMsg = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.broadcast.blacklist.attempted-join.message");

                String broadcast = broadcastMsg.replace("%player%", profile.getName());
                String hover = ChatUtil.buildDescription(hoverMsg)
                        .replace("%ban_date%", TimeUtil.formatDateDiff(profile.getActiveBlacklist().getAddedAt()))
                        .replace("%banned_by%", profile.getActiveBlacklist().getAddedBy())
                        .replace("%reason%", profile.getActiveBlacklist().getAddedReason());

                ComponentBuilder builder = (new ComponentBuilder(CC.translate(broadcast)));
                builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(hover)).create()));
                player.spigot().sendMessage(builder.create());
            }
        } else if (type == PunishmentType.BAN) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!player.hasPermission("oxygen.punishments.silent")) continue;

                List<String> hoverMsg = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("punishments.broadcast.ban.attempted-join.hover");
                String broadcastMsg = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.broadcast.ban.attempted-join.message");

                String broadcast = broadcastMsg.replace("%player%", profile.getName());
                String hover = ChatUtil.buildDescription(hoverMsg)
                        .replace("%ban_date%", TimeUtil.formatDateDiff(profile.getActiveBan().getAddedAt()))
                        .replace("%banned_by%", profile.getActiveBan().getAddedBy())
                        .replace("%reason%", profile.getActiveBan().getAddedReason())
                        .replace("%remaining%", profile.getActiveBan().getRemaining())
                        .replace("%duration%", TimeUtil.getDetailedTime(profile.getActiveBan().getDuration()));

                ComponentBuilder builder = (new ComponentBuilder(CC.translate(broadcast)));
                builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(hover)).create()));
                player.spigot().sendMessage(builder.create());
            }
        }
    }

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
