package dev.hely.core.commands.implement.punishment.undo;

import com.google.common.collect.ImmutableMap;
import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.log.LoggerTracker;
import dev.hely.core.profile.log.types.LogActionTypes;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.packet.implement.PunishmentRemovePacket;
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name Oxygen
 */
public class UnBlacklistCommand extends BaseCommand {

    public UnBlacklistCommand(){
        super("unblacklist","oxygen.punishment.unblacklist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_UNBLACKLIST_USAGE));
            return;
        }

        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getActiveBlacklist() == null) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_UNBLACKLIST_NOT_BLACKLISTED));
            return;
        }

        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", 2);

        Punishment punishment = profile.getActiveBlacklist();

        punishment.setRemoved(true);
        punishment.setRemovedBy(sender.getName());
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedReason(reason
                .replace("-s", ""));


        punishment.setSilent(reason.contains("-s"));
        punishment.save();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            LoggerTracker.logPlayerAction(player.getUniqueId(), LogActionTypes.UNBLACKLISTED_PLAYER, ImmutableMap.of(
                    "staffMember", player.getName(),
                    "unblacklistedPlayer", profile.getName(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));

            LoggerTracker.logPlayerAction(profile.getUniqueId(), LogActionTypes.PUNISHMENT_REMOVED, ImmutableMap.of(
                    "unblacklistedPlayer", profile.getName(),
                    "staffMember", player.getName(),
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        } else {
            LoggerTracker.logPlayerAction(profile.getUniqueId(), LogActionTypes.PUNISHMENT_REMOVED, ImmutableMap.of(
                    "unblacklistedPlayer", profile.getName(),
                    "staffMember", "console",
                    "reason", punishment.getAddedReason(),
                    "silent", punishment.isSilent()
            ));
        }

        Oxygen.getInstance().getRedisModule().sendPacket(new PunishmentRemovePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
