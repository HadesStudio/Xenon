package dev.hely.core.bukkit.commands.implement.punishment;

import com.google.common.collect.ImmutableMap;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.global.profile.Profile;
import dev.hely.core.global.profile.log.LoggerTracker;
import dev.hely.core.global.profile.log.types.LogActionTypes;
import dev.hely.core.global.punishment.Punishment;
import dev.hely.core.global.punishment.PunishmentType;
import dev.hely.core.global.punishment.packet.implement.PunishmentExecutePacket;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name OxygenGlobal
 */
public class KickCommand extends BaseCommand {

    public KickCommand(){
        super("kick", "oxygen.punishment.kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_KICK_USAGE));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, player, args[0])) return;

        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", 2);

        Punishment punishment = new Punishment();
        punishment.setUniqueId(UUID.randomUUID());
        punishment.setType(PunishmentType.KICK);

        punishment.setAddedBy(sender.getName());
        punishment.setAddedServer(OxygenBukkit.getInstance().getServerManager().getServerName());
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setAddedReason(reason
                .replace("-s", ""));

        punishment.setTargetUUID(profile.getUniqueId());

        punishment.setSilent(reason.contains("-s"));
        punishment.save();

        if (sender instanceof Player) {
            Player sender_player = (Player) sender;
            LoggerTracker.logPlayerAction(sender_player.getUniqueId(), LogActionTypes.KICKED_PLAYER, ImmutableMap.of(
                    "staffMember", sender_player.getName(),
                    "kickedPlayer", player.getName(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));

            LoggerTracker.logPlayerAction(player.getUniqueId(), LogActionTypes.PUNISHMENT_RECEIVED, ImmutableMap.of(
                    "kickedPlayer", player.getName(),
                    "staffMember", sender_player.getName(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        } else {
            LoggerTracker.logPlayerAction(player.getUniqueId(), LogActionTypes.PUNISHMENT_RECEIVED, ImmutableMap.of(
                    "kickedPlayer", player.getName(),
                    "staffMember", "console",
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
