package dev.hely.core.global.ranks.rank.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.global.ranks.rank.packet.implement.RankDeletePacket;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name OxygenGlobal
 */
public class RankDeleteCommand extends SubCommand {

    public RankDeleteCommand() {
        super("delete", Collections.singletonList("remove"), "oxygen.rank.delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.delete.usage")));
            return;
        }

        if (OxygenBukkit.getInstance().getRankManager().getRank(args[1]) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        OxygenBukkit.getInstance().getRankManager().deleteRank(OxygenBukkit.getInstance().getRankManager().getRank(args[1]));
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.delete.deleted")
                .replace("%rank_name%", args[1])));

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankDeletePacket(args[1]));

        // RELOAD ALL PROFILE ATTACHMENTS
    }
}