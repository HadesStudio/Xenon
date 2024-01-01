package net.ascend.core.bukkit.commands.implement.punishment.undo;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.types.LogActionTypes;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.packet.implement.PunishmentRemovePacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name Xenon
 */
public class UnBlacklistCommand extends BaseCommand {

    public UnBlacklistCommand(){
        super("unblacklist","xenon.punishment.unblacklist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_UNBLACKLIST_USAGE));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
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
            XenonBukkit.getInstance().getLogManager().logPlayerAction(player.getUniqueId(), player.getName(), Arrays.asList(
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Unblacklisted Player&7: &a" + profile.getName(),
                    "&6Reason&7: &a" + punishment.getAddedReason(),
                    "&6Silent&7: &a" + punishment.isSilent()
            ),LogActionTypes.UNBLACKLISTED_PLAYER, LogCategories.PUNISHMENT_LOGS);
            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Unblacklisted Player&7: &a" + profile.getName(),
                    "&6Reason&7: &a" + punishment.getAddedReason(),
                    "&6Silent&7: &a" + punishment.isSilent()
            ),LogActionTypes.PUNISHMENT_REMOVED, LogCategories.PUNISHMENT_LOGS);
        } else {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Staff Member&7: &4Console",
                    "&6Unblacklisted Player&7: &a" + profile.getName(),
                    "&6Reason&7: &a" + punishment.getAddedReason(),
                    "&6Silent&7: &a" + punishment.isSilent()
            ),LogActionTypes.PUNISHMENT_REMOVED, LogCategories.PUNISHMENT_LOGS);
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new PunishmentRemovePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
