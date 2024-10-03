package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankRenameCommand extends SubCommand {

    public RankRenameCommand() {
        super("rename", Collections.singletonList("changename"), "xenon.rank.rename");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.rename.usage")));
            return;
        }

        Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[0]);
        if (rank == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        Rank newRank = XenonBukkit.getInstance().getRankManager().getRank(args[1]);
        if (newRank != null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.already-exist")));
            return;
        }

        String old_name = rank.getName();
        // rank.setName(args[2]); FINISHED LEANDRO FIX
        rank.save();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.rename.renamed")
                .replace("%rank%", old_name)
                .replace("%new_name%", args[1])));

        XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[0]));
    }
}
