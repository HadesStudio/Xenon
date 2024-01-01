package net.ascend.core.bukkit.commands.implement.staff.teleport;

import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class TeleportCommand extends BaseCommand {
    public TeleportCommand() {
        super("teleport", Collections.singletonList("tp"), "xenon.command.teleport", true);
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
