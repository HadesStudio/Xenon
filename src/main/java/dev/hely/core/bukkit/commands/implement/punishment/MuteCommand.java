package dev.hely.core.bukkit.commands.implement.punishment;

import com.google.common.collect.ImmutableMap;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.log.LoggerTracker;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.punishment.PunishmentType;
import dev.hely.core.bukkit.punishment.packet.implement.PunishmentExecutePacket;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name OxygenGlobal
 */
public class MuteCommand extends BaseCommand {

    public MuteCommand(){
        super("mute", Arrays.asList("tempmute"), "oxygen.punishment.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_MUTE_USAGE));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getActiveMute() != null) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_MUTE_ALREADY_MUTED));
            return;
        }


        long duration = -1L;

        if (args.length > 1) {
            if (!args[1].equalsIgnoreCase("perm") && !args[1].equalsIgnoreCase("permanent")) {
                duration = TimeUtil.parseTime(args[1]);
            }
        }


        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", duration == -1L ? 2 : 3);

        Punishment punishment = new Punishment();
        punishment.setUniqueId(UUID.randomUUID());
        punishment.setType(PunishmentType.MUTE);

        punishment.setDuration(duration);

        punishment.setAddedBy(sender.getName());
        punishment.setAddedServer(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setAddedReason(reason
                .replace("-s", ""));

        punishment.setTargetUUID(profile.getUniqueId());

        punishment.setSilent(reason.contains("-s"));
        punishment.save();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            LoggerTracker.logPlayerAction(player.getUniqueId(), LogActionTypes.MUTED_PLAYER, ImmutableMap.of(
                    "staffMember", player.getName(),
                    "mutedPlayer", profile.getName(),
                    "duration", punishment.getDuration(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));

            LoggerTracker.logPlayerAction(profile.getUniqueId(), LogActionTypes.PUNISHMENT_RECEIVED, ImmutableMap.of(
                    "mutedPlayer", profile.getName(),
                    "staffMember", player.getName(),
                    "duration", punishment.getDuration(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        } else {
            LoggerTracker.logPlayerAction(profile.getUniqueId(), LogActionTypes.PUNISHMENT_RECEIVED, ImmutableMap.of(
                    "mutedPlayer", profile.getName(),
                    "staffMember", "console",
                    "duration", punishment.getDuration(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        }

        OxygenBukkit.getInstance().getProfileManager().sendMutePunishmentNotification(profile.getUniqueId(), punishment);
        OxygenBukkit.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
