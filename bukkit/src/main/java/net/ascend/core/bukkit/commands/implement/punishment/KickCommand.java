package net.ascend.core.bukkit.commands.implement.punishment;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.LogType;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.punishment.packet.implement.PunishmentExecutePacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class KickCommand extends BaseCommand {

    public KickCommand(){
        super("kick", "xenon.punishment.kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(Locale.PUNISHMENT_KICK_USAGE));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
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

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        String reason = args.length == 1 ? Locale.PUNISHMENTS_DEFAULT_REASON : ChatUtil.joinArray(args, " ", 2);

        Punishment punishment = new Punishment();
        punishment.setUniqueId(UUID.randomUUID());
        punishment.setType(PunishmentType.KICK);

        punishment.setAddedBy((sender instanceof Player ? ((Player) sender).getUniqueId() : null));
        punishment.setAddedServer(XenonBukkit.getInstance().getServerManager().getCurrentServerName());
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setAddedReason(reason
                .replace("-s", ""));

        punishment.setTargetUUID(profile.getUniqueId());

        punishment.setSilent(reason.contains("-s"));
        punishment.save();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            XenonBukkit.getInstance().getLogManager().logPlayerAction(player.getUniqueId(), player.getName(), Arrays.asList(
                    "&6Kicked Player&7: &c" + player.getName(),
                    "&6Reason&7: &c" + punishment.getAddedReason(),
                    "&6Silent&7: &c" + punishment.isSilent()
            ), LogType.KICKED_PLAYER, LogCategories.PUNISHMENT_LOGS);

            XenonBukkit.getInstance().getLogManager().logPlayerAction(target.getUniqueId(), target.getName(), Arrays.asList(
                    "&6Type&7: &cKick",
                    "&6Staff Member&7: &c" + player.getName(),
                    "&6Reason&7: &c" + punishment.getAddedReason(),
                    "&6Silent&7: &c" + punishment.isSilent()
            ), LogType.PUNISHMENT_RECEIVED, LogCategories.PUNISHMENT_LOGS);
        } else {
            XenonBukkit.getInstance().getLogManager().logPlayerAction(target.getUniqueId(), target.getName(), Arrays.asList(
                    "&6Type&7: &cKick",
                    "&6Staff Member&7: &4Console",
                    "&6Reason&7: &c" + punishment.getAddedReason(),
                    "&6Silent&7: &c" + punishment.isSilent()
            ), LogType.PUNISHMENT_RECEIVED, LogCategories.PUNISHMENT_LOGS);
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new PunishmentExecutePacket(punishment.getUniqueId(), sender.getName(), profile.getUniqueId()));
    }
}
