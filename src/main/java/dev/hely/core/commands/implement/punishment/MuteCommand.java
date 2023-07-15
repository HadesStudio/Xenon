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

import java.util.Arrays;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name Oxygen
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

        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[0]);
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
                duration = TimeUtil.parse(args[1]);
            }
        }


        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", duration == -1L ? 2 : 3);

        Punishment punishment = new Punishment();
        punishment.setUniqueId(UUID.randomUUID());
        punishment.setType(PunishmentType.MUTE);

        punishment.setDuration(duration);

        punishment.setAddedBy(sender.getName());
        punishment.setAddedServer(Oxygen.getInstance().getServerManager().getServerName());
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setAddedReason(reason
                .replace("-s", ""));

        punishment.setTargetUUID(profile.getUniqueId());

        punishment.setSilent(reason.contains("-s"));
        punishment.save();

        Oxygen.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
