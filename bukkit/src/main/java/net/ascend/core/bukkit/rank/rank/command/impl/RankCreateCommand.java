package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankCreateCommand extends SubCommand {
    public RankCreateCommand() {
        super("create", Collections.singletonList("new"), "xenon.rank.create");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.usage")));
            return;
        }

        if (XenonBukkit.getInstance().getRankManager().getRank(args[0]) != null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.already-exist")));
            return;
        }

        XenonBukkit.getInstance().getRankManager().createRank(args[0], false);
        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.create.created").replace("%rank_name%", args[0])));

        XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[0]));
    }
}

