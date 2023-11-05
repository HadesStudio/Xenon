package dev.hely.core.bukkit.commands.implement.staff.teleport;

import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAllCommand extends BaseCommand {
    public TeleportAllCommand() {
        super("tpall", "oxygen.command.teleport.all", true);
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
