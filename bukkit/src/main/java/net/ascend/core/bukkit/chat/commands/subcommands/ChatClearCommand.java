package net.ascend.core.bukkit.chat.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ChatClearCommand extends SubCommand {

    public ChatClearCommand() {
        super("clear", Collections.singletonList("clearchat"), "xenon.staff.chat.clear");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        for (int i = 0; i < 100; i++) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("xenon.staff")) {
                    player.sendMessage(" ");
                }
            }
        }


        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.clear.cleared")));
        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.clear.broadcast")) {
            sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}
