package net.ascend.core.bukkit.commands.implement.punishment;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.types.LogActionTypes;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.punishment.packet.implement.PunishmentExecutePacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name Xenon
 */
public class BanCommand extends BaseCommand {

    public BanCommand(){
        super("ban","xenon.punishment.ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_BAN_USAGE));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }
        if (profile.getActiveBan() != null) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_BAN_ALREADY_BANNED));
            return;
        }
        if (sender instanceof Player) {
            Player senderPlayer = (Player)sender;
            Profile senderProfile = XenonBukkit.getInstance().getProfileManager().getProfile(senderPlayer.getUniqueId());
            if (!senderProfile.canInteact(profile)) {
                senderPlayer.sendMessage(CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("punishments.safety.cannot-punish")));
                return;
            }
        }

        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", 2);

        Punishment punishment = new Punishment();
        punishment.setUniqueId(UUID.randomUUID());
        punishment.setType(PunishmentType.BAN);

        punishment.setAddedBy(sender.getName());
        punishment.setAddedServer(XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
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
            XenonBukkit.getInstance().getLogManager().logPlayerAction(player.getUniqueId(), player.getName(), Arrays.asList(
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Banned Player&7: &a" + profile.getName(),
                    "&6Reason&7: &a" + punishment.getAddedReason(),
                    "&6Duration&7: &cPermanent",
                    "&6Silent&7: &a" + punishment.isSilent()
            ),LogActionTypes.BANNED_PLAYER, LogCategories.PUNISHMENT_LOGS);

            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Banned Player&7: &a" + profile.getName(),
                    "&6Reason&7: &a" + punishment.getAddedReason(),
                    "&6Duration&7: &cPermanent",
                    "&6Silent&7: &a" + punishment.isSilent()
            ),LogActionTypes.PUNISHMENT_RECEIVED, LogCategories.PUNISHMENT_LOGS);
        } else {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Staff Member&7: &4Console",
                    "&6Banned Player&7: &a" + profile.getName(),
                    "&6Reason&7: &a" + punishment.getAddedReason(),
                    "&6Duration&7: &cPermanent",
                    "&6Silent&7: &a" + punishment.isSilent()
            ),LogActionTypes.PUNISHMENT_RECEIVED, LogCategories.PUNISHMENT_LOGS);
        }
        XenonBukkit.getInstance().getProfileManager().sendBanPunishmentNotification(profile.getUniqueId(), punishment);
        XenonBukkit.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
