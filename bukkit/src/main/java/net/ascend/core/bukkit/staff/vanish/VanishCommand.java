package net.ascend.core.bukkit.staff.vanish;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class VanishCommand extends BaseCommand {

    public VanishCommand() {
        super("vanish", Arrays.asList("v"), "xenon.vanish");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

            if (profile.getStaffProfile().isVanish()) {
                XenonBukkit.getInstance().getVanishManager().disabledVanish(player);
                //player.sendMessage(CC.translate(Locale.VANISH_COMMAND_DISABLED));
            } else {
                XenonBukkit.getInstance().getVanishManager().enabledVanish(player);
                //player.sendMessage(CC.translate(Locale.VANISH_COMMAND_ENABLED));
            }
        } else if (sender.hasPermission("hcf.command.vanish.others")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (!this.checkPlayer(sender, target, args[0])) return;

            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(target.getUniqueId());

            if (profile.getStaffProfile().isVanish()) {
                XenonBukkit.getInstance().getVanishManager().disabledVanish(target.getPlayer());
                //target.sendMessage(CC.translate(Locale.VANISH_COMMAND_DISABLED_OTHERS.replace("%player%", sender.getName())));
            } else {
                XenonBukkit.getInstance().getVanishManager().enabledVanish(target);

                //target.sendMessage(CC.translate(Locale.VANISH_COMMAND_ENABLED_OTHERS.replace("%player%", sender.getName())));
            }
        }
    }
}