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
public class DayCommand extends BaseCommand {

    public DayCommand(){
        super("day", "oxygen.day", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        profile.setTime("DAY");
        player.setPlayerTime(0L, false);

        player.sendMessage(CC.translate(Locale.DAY_SET));
    }
}
