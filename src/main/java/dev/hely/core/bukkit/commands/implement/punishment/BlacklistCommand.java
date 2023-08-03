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
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name OxygenGlobal
 */
public class BlacklistCommand extends BaseCommand {

    public BlacklistCommand(){
        super("blacklist", Collections.singletonList("blacklistplayer"), "oxygen.punishment.blacklist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_BLACKLIST_USAGE));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getActiveBlacklist() != null) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_BLACKLIST_ALREADY_BLACKLISTED));
            return;
        }

        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", 2);

        Punishment punishment = new Punishment();
        punishment.setUniqueId(UUID.randomUUID());
        punishment.setType(PunishmentType.BLACKLIST);

        punishment.setAddedBy(sender.getName());
        punishment.setAddedServer(OxygenBukkit.getInstance().getServerManager().getServerName());
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setAddedReason(reason
                .replace("-s", "")
                .replace("-c", ""));

        punishment.setTargetUUID(profile.getUniqueId());

        punishment.setClear(reason.contains("-c"));
        punishment.setSilent(reason.contains("-s"));
        punishment.save();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            LoggerTracker.logPlayerAction(player.getUniqueId(), LogActionTypes.BLACKLISTED_PLAYER, ImmutableMap.of(
                    "staffMember", player.getName(),
                    "blacklistedPlayer", profile.getName(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));

            LoggerTracker.logPlayerAction(profile.getUniqueId(), LogActionTypes.PUNISHMENT_RECEIVED, ImmutableMap.of(
                    "blacklistedPlayer", profile.getName(),
                    "staffMember", player.getName(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        } else {
            LoggerTracker.logPlayerAction(profile.getUniqueId(), LogActionTypes.PUNISHMENT_RECEIVED, ImmutableMap.of(
                    "blacklistedPlayer", profile.getName(),
                    "staffMember", "console",
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
