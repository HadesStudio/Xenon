package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankSetWeightCommand extends SubCommand {

    public RankSetWeightCommand() {
        super("setweight", Collections.singletonList("weight"), "xenon.rank.setweight");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setweight.usage")));
            return;
        }

        Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[0]);
        if (rank == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        if (!this.checkNumber(sender, args[1])) return;
        int weight = Math.abs(Integer.parseInt(args[1]));

        rank.setWeight(weight);
        rank.save();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.setweight.set")
                .replace("%weight%", String.valueOf(weight))
        ));

        XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[0]));
    }
}
