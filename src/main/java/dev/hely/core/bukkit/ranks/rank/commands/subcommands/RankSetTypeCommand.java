package dev.hely.core.bukkit.ranks.rank.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.ranks.rank.RankType;
import dev.hely.core.bukkit.ranks.rank.packet.implement.RankUpdatePacket;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 08/08/2023
 * Protect Name Oxygen
 */
public class RankSetTypeCommand extends SubCommand {

    public RankSetTypeCommand() {
        super("settype", Collections.singletonList("weight"), "oxygen.rank.settype");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.settype.usage")));
            return;
        }

        Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[1]);
        if (rank == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.dont-exists")));
            return;
        }

        RankType type;

        try {
            type = RankType.valueOf(args[2].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.settype.usage")));
            return;
        }

        rank.setType(type);
        rank.save();

        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.settype.set")
                .replace("%type%", type.name())
        ));

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[1]));
    }
}
