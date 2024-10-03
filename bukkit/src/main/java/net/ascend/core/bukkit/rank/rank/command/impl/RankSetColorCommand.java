package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankSetColorCommand extends SubCommand {
    public RankSetColorCommand() {
        super("setcolor", Collections.singletonList("color"), "xenon.rank.color");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.color.usage")));
            return;
        }

        Rank rank = XenonBukkit.getInstance().getRankManager().getRank(args[0]);
        if (rank == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.color.not-exist")));
            return;
        }

        ChatColor color;
        try {
            color = ChatColor.valueOf(args[1]);
        } catch (Exception e) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.color.invalid-color")));
            return;
        }

        rank.setDisplay_color(color);
        rank.save();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.color.color-changed")
                .replace("%rank%", rank.getName())
                .replace("%color%", color.name())));
        XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(args[0]));
    }
}

