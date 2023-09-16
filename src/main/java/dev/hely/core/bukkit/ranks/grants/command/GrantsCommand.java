package dev.hely.core.bukkit.ranks.grants.command;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.menu.submenus.GrantsMenu;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created By LeandroSSJ
 * Created on 06/08/2023
 * Protect Name Oxygen
 */
public class GrantsCommand extends BaseCommand {

    public GrantsCommand(){
        super("grants", "oxygen.grants", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /grants <player>"));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        new GrantsMenu(profile, false).openMenu(player);
    }
}

