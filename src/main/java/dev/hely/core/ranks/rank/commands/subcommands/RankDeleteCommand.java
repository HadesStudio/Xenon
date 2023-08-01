package dev.hely.core.ranks.rank.commands.subcommands;

import dev.hely.core.Oxygen;
import dev.hely.core.ranks.rank.Rank;
import dev.hely.core.ranks.rank.packet.implement.RankDeletePacket;
import dev.hely.core.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name Oxygen
 */
public class RankDeleteCommand extends SubCommand {

    public RankDeleteCommand() {
        super("delete", Collections.singletonList("remove"), "oxygen.rank.delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.delete.usage")));
            return;
        }

        if (Oxygen.getInstance().getRankManager().getRank(args[1]) == null) {
            sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        Oxygen.getInstance().getRankManager().deleteRank(Oxygen.getInstance().getRankManager().getRank(args[1]));
        sender.sendMessage(CC.translate(Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.delete.deleted")
                .replace("%rank_name%", args[1])));

        Oxygen.getInstance().getRedisModule().sendPacket(new RankDeletePacket(args[1]));

        // RELOAD ALL PROFILE ATTACHMENTS
    }
}