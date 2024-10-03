package net.ascend.core.bukkit.commands.implement.player;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.NameMCUtils;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class NameMCCommand extends BaseCommand {

    public NameMCCommand(){
        super("namemc", Collections.singletonList("vote"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (NameMCUtils.isVerified(player.getUniqueId())) {
            NameMCUtils.runCommands(player);
            for (Player players : Bukkit.getOnlinePlayers()) {
                for (String message : XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.messages.broadcast")) {
                    players.sendMessage(CC.translate(message).replace("%player%", player.getName()));
                }
            }
            for (String message : XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.messages.linked")) {
                player.sendMessage(CC.translate(message).replace("%player%", player.getName()));
            }
        } else {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("name-mc.messages.not-linked")));
        }
    }
}
