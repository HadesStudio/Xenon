package dev.hely.core.commands.implement.punishment.undo;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.core.punishment.packet.implement.PunishmentExecutePacket;
import dev.hely.core.punishment.packet.implement.PunishmentRemovePacket;
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name Oxygen
 */
public class UnBanCommand extends BaseCommand {

    public UnBanCommand(){
        super("unban","oxygen.punishment.unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_UNBAN_USAGE));
            return;
        }

        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getActiveBan() == null) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_UNBAN_NOT_BANNED));
            return;
        }

        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", 2);

        Punishment punishment = profile.getActiveBan();

        punishment.setRemoved(true);
        punishment.setRemovedBy(sender.getName());
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedReason(reason
                .replace("-s", ""));


        punishment.setSilent(reason.contains("-s"));
        punishment.save();

        Oxygen.getInstance().getRedisModule().sendPacket(new PunishmentRemovePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
