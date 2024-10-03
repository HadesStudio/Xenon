package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.menu.RankListMenu;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankListCommand extends SubCommand {

    public RankListCommand() {
        super("list", "xenon.rank.list", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (XenonBukkit.getInstance().getRankManager().getRanks().isEmpty()) {
            player.sendMessage(CC.translate("&cSomehow, their is no ranks created, please open a Xenon Development discord ticket, as this should automatically create a default rank.."));
            return;
        }

        new RankListMenu().openMenu(player);
    }
}
