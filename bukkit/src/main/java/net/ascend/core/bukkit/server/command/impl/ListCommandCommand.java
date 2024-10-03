package net.ascend.core.bukkit.server.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.server.menu.ServerListMenu;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommandCommand extends SubCommand {

    public ListCommandCommand(){
        super("list", "xenon.command.servermanager.argument.list");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof Player) {
            new ServerListMenu().openMenu(((Player) sender));
        } else {
            Locale.SERVER_MANAGER_LIST_MESSAGE.forEach(message -> {
                if (!message.toLowerCase().contains("%server%")) {
                    sender.sendMessage(CC.translate(message));
                } else {
                    XenonBukkit.getInstance().getServerManager().getServers().forEach(server -> {
                        sender.sendMessage(CC.translate(Locale.SERVER_MANAGER_LIST_FORMAT)
                                .replace("%server_name%", server.getName())
                                .replace("%server_status%", server.getState().getName())
                                .replace("%online_players%", String.valueOf(server.getOnlinePlayers()))
                                .replace("%max_players%", Integer.toString(server.getMaxPlayers()))
                        );
                    });
                }
            });
        }
    }
}
