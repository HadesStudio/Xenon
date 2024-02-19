package net.ascend.core.bukkit.commands.implement.staff;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class VanishCommand extends BaseCommand {
    public VanishCommand() {
        super("vanish", Arrays.asList("v"), "xenon.command.vanish");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            if (XenonBukkit.getInstance().getStaffManager().isVanished(player)) {
                XenonBukkit.getInstance().getStaffManager().disabledVanish(player);
                player.sendMessage(CC.translate(Locale.VANISH_DISABLED));
            } else {
                XenonBukkit.getInstance().getStaffManager().enabledVanish(player);
                player.sendMessage(CC.translate(Locale.VANISH_ENABLED));
            }
        } else if (sender.hasPermission("xenon.command.vanish.others")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (!this.checkPlayer(sender, target, args[0])) return;

            if (XenonBukkit.getInstance().getStaffManager().isVanished(target.getPlayer())) {
                XenonBukkit.getInstance().getStaffManager().disabledVanish(target.getPlayer());
                target.getPlayer().sendMessage(CC.translate(Locale.VANISH_DISABLED_O.replace("%player%", sender.getName())));
            } else {
                XenonBukkit.getInstance().getStaffManager().enabledVanish(target.getPlayer());

                target.getPlayer().sendMessage(CC.translate(Locale.VANISH_ENABLED_O.replace("%player%", sender.getName())));
            }
        }
    }
}
