package net.ascend.core.bukkit.commands.implement.staff.teleport;

import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeleportHereCommand extends BaseCommand {
    public TeleportHereCommand() {
        super("tphere", Arrays.asList("tpahere", "s", "teleporthere"), "xenon.command.teleport.here", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(CC.translate(Locale.TELEPORT_HERE_USAGE));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!this.checkPlayer(sender, target, args[0])) return;

            target.teleport(player);
            player.sendMessage(CC.translate(Locale.TELEPORT_HERE_TO.replace("%player%", target.getName())));
            target.sendMessage(CC.translate(Locale.TELEPORT_HERE_FROM.replace("%player%", player.getName())));
        }
    }
}
