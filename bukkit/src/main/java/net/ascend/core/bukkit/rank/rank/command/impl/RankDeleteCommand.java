package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankDeletePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankDeleteCommand extends SubCommand {

    public RankDeleteCommand() {
        super("delete", Collections.singletonList("remove"), "xenon.rank.delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.delete.usage")));
            return;
        }

        Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[0]);
        if (rank == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        XenonBukkit.getInstance().getRankManager().deleteRank(rank);
        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.delete.deleted")
                .replace("%rank_name%", args[0])));

        XenonBukkit.getInstance().getRedisModule().sendPacket(new RankDeletePacket(args[0]));

        // RELOAD ALL PROFILE ATTACHMENTS
    }
}