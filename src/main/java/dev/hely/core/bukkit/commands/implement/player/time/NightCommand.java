package dev.hely.core.bukkit.commands.implement.player.time;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created By LeandroSSJ
 * Created on 06/09/2023
 * Protect Name Oxygen
 */
public class NightCommand extends BaseCommand {

    public NightCommand(){
        super("night", "oxygen.night", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        profile.setTime("NIGHT");
        player.setPlayerTime(20000L, false);

        player.sendMessage(CC.translate(Locale.NIGHT_SET));
    }
}
