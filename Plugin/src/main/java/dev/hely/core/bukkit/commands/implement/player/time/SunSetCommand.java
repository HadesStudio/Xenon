package dev.hely.core.bukkit.commands.implement.player.time;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created By LeandroSSJ
 * Created on 06/09/2023
 * Protect Name Oxygen
 */
public class SunSetCommand extends BaseCommand {

    public SunSetCommand(){
        super("sunset", "oxygen.sunset", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        profile.setTime("SUNSET");
        player.setPlayerTime(12500L, false);

        player.sendMessage(CC.translate(Locale.SUNSET_SET));
    }
}
