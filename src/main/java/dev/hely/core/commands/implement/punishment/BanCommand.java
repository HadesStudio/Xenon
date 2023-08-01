package dev.hely.core.commands.implement.punishment;

import com.google.common.collect.ImmutableMap;
import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.log.LoggerTracker;
import dev.hely.core.profile.log.types.LogActionTypes;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.core.punishment.packet.implement.PunishmentExecutePacket;
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.TimeUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name Oxygen
 */
public class BanCommand extends BaseCommand {

    public BanCommand(){
        super("ban","oxygen.punishment.ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_BAN_USAGE));
            return;
        }

        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getActiveBan() != null) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_BAN_ALREADY_BANNED));
            return;
        }

        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", 2);

        Punishment punishment = new Punishment();
        punishment.setUniqueId(UUID.randomUUID());
        punishment.setType(PunishmentType.BAN);

        punishment.setAddedBy(sender.getName());
        punishment.setAddedServer(Oxygen.getInstance().getServerManager().getServerName());
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setAddedReason(reason
                .replace("-s", "")
                .replace("-c", ""));

        punishment.setTargetUUID(profile.getUniqueId());

        punishment.setClear(reason.contains("-c"));
        punishment.setSilent(reason.contains("-s"));
        punishment.setBan_ID(String.valueOf(Math.floor(Math.random() * (1000 - 10000 + 1) + 1000)));
        punishment.save();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            LoggerTracker.logPlayerAction(player.getUniqueId(), LogActionTypes.BANNED_PLAYER, ImmutableMap.of(
                    "staffMember", player.getName(),
                    "bannedPlayer", profile.getName(),
                    "duration", "permanent",
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));

            LoggerTracker.logPlayerAction(profile.getUniqueId(), LogActionTypes.PUNISHMENT_RECEIVED, ImmutableMap.of(
                    "bannedPlayer", profile.getName(),
                    "staffMember", player.getName(),
                    "duration", "permanent",
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        } else {
            LoggerTracker.logPlayerAction(profile.getUniqueId(), LogActionTypes.PUNISHMENT_RECEIVED, ImmutableMap.of(
                    "bannedPlayer", profile.getName(),
                    "staffMember", "console",
                    "duration", "permanent",
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        }
        Oxygen.getInstance().getProfileManager().sendBanPunishmentNotification(profile.getUniqueId(), punishment);
        Oxygen.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
