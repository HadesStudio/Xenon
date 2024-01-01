package net.ascend.core.bukkit.commands.implement.staff.teleport;

import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAllCommand extends BaseCommand {
    public TeleportAllCommand() {
        super("tpall", "xenon.command.teleport.all", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        for (Player target : Bukkit.getOnlinePlayers()) {
            target.teleport(player);
            if (!target.equals(player)) {
                target.sendMessage(CC.translate(Locale.TELEPORT_ALL_FROM.replace("%player%", player.getName())));
            }
        }
        player.sendMessage(CC.translate(Locale.TELEPORT_ALL_TO));
    }
}
