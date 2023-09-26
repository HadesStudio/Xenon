package dev.hely.core.bukkit.commands.implement.staff.teleport;

import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class TeleportCommand extends BaseCommand {
    public TeleportCommand() {
        super("teleport", Collections.singletonList("tp"), "oxygen.command.teleport", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(CC.translate(Locale.TELEPORT_TO_USAGE));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!this.checkPlayer(sender, target, args[0])) return;

            player.teleport(target);
            player.sendMessage(CC.translate(Locale.TELEPORT_TO.replace("%player%", target.getName())));
        }
    }
}
