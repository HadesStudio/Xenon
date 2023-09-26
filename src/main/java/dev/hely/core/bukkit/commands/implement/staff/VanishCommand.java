package dev.hely.hcf.command.staff;

import dev.hely.hcf.Void;
import dev.hely.hcf.configuration.Language;
import dev.hely.hcf.command.base.BaseCommand;
import dev.hely.hcf.library.api.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Leandro Figueroa (LeandroSSJ)
 * domingo, abril 25, 2021
 */
public class VanishCommand extends BaseCommand {
    public VanishCommand() {
        super("vanish", Arrays.asList("v"), "hcf.command.vanish");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            if (Void.getInstance().getStaffModeManager().isVanished(player)) {
                Void.getInstance().getStaffModeManager().disabledVanish(player);
                player.sendMessage(CC.translate(Language.VANISH_DISABLED));
            } else {
                Void.getInstance().getStaffModeManager().enabledVanish(player);
                player.sendMessage(CC.translate(Language.VANISH_ENABLED));
            }
        } else if (sender.hasPermission("hcf.command.vanish.others")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (!this.checkPlayer(sender, target, args[0])) return;

            if (Void.getInstance().getStaffModeManager().isVanished(target.getPlayer())) {
                Void.getInstance().getStaffModeManager().disabledVanish(target.getPlayer());
                target.getPlayer().sendMessage(CC.translate(Language.VANISH_DISABLED_O.replace("%player%", sender.getName())));
            } else {
                Void.getInstance().getStaffModeManager().enabledVanish(target.getPlayer());

                target.getPlayer().sendMessage(CC.translate(Language.VANISH_ENABLED_O.replace("%player%", sender.getName())));
            }
        }
    }
}
