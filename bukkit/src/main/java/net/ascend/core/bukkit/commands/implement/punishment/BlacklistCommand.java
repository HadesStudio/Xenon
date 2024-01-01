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
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name Xenon
 */
public class BlacklistCommand extends BaseCommand {

    public BlacklistCommand(){
        super("blacklist", Collections.singletonList("blacklistplayer"), "xenon.punishment.blacklist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_BLACKLIST_USAGE));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getActiveBlacklist() != null) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_BLACKLIST_ALREADY_BLACKLISTED));
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
        punishment.setType(PunishmentType.BLACKLIST);

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

            XenonBukkit.getInstance().getLogManager().logPlayerAction(
                    player.getUniqueId(),
                    player.getName(),
                    Arrays.asList(
                    "&6Staff Member&7: &a" + player.getName(),
                    "&6Blacklisted Player&7: &a" + profile.getName(),
                    "&6Reason&7: &a" + punishment.getAddedReason(),
                    "&6Silent&7: &a" + punishment.isSilent()
                    ),
                    LogActionTypes.BLACKLISTED_PLAYER, LogCategories.PUNISHMENT_LOGS);
        } else {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                    "&6Staff Member&7: &4Console",
                    "&6Blacklisted Player&7: &a" + profile.getName(),
                    "&6Reason&7: &a" + punishment.getAddedReason(),
                    "&6Silent&7: &a" + punishment.isSilent()
            ),LogActionTypes.BLACKLISTED_PLAYER, LogCategories.PUNISHMENT_LOGS);
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
