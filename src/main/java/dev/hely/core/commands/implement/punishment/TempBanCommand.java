package dev.hely.core.commands.implement.punishment;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.core.punishment.packet.implement.PunishmentExecutePacket;
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.TimeUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name Oxygen
 */
public class TempBanCommand extends BaseCommand {

    public TempBanCommand(){
        super("tempban", Collections.singletonList("tban"), "oxygen.punishment.tempban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_TEMPBAN_USAGE));
            return;
        }

        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getActiveBan() != null) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_TEMPBAN_ALREADY_BANNED));
            return;
        }

        long duration = TimeUtil.parse(args[1]);
        if(duration == -1) {
            sender.sendMessage(CC.translate("&cFailed to parse the given duration string."));
            sender.sendMessage(CC.translate("&cExample Syntax: 1m2w3d4h (1 month, 2 weeks, 3 days, and 4 hours)"));
            return;
        }


        String reason = args.length == 2 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", 3);

        Punishment punishment = new Punishment();
        punishment.setUniqueId(UUID.randomUUID());
        punishment.setType(PunishmentType.BAN);

        punishment.setDuration(duration);

        punishment.setAddedBy(sender.getName());
        punishment.setAddedServer(Oxygen.getInstance().getServerManager().getServerName());
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setAddedReason(reason
                .replace("-s", "")
                .replace("-c", ""));

        punishment.setTargetUUID(profile.getUniqueId());

        punishment.setClear(reason.contains("-c"));
        punishment.setSilent(reason.contains("-s"));
        punishment.save();

        Oxygen.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
