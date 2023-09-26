package dev.hely.core.bukkit.commands.implement.staff.teleport;

import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportPossCommand extends BaseCommand {
    public boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public TeleportPossCommand() {
        super("tppos", "oxygen.command.teleport.position", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 3) {
            player.sendMessage(CC.translate(Locale.TELEPORT_POS_USAGE));
        } else {
            if (!(this.isDouble(args[0]) && this.isDouble(args[1]) && this.isDouble(args[2]))) {
                player.sendMessage(CC.translate(Locale.TELEPORT_POS_USAGE));
            } else {
                double x = Double.parseDouble(args[0]) + 0.5;
                double y = Double.parseDouble(args[1]) + 0.5;
                double z = Double.parseDouble(args[2]) + 0.5;
                Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());

                player.teleport(loc);
                player.sendMessage(CC.translate(Locale.TELEPORT_POSITION.replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z))));
            }
        }
    }
}