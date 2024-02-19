package net.ascend.core.bukkit.commands.implement.player.time;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created By LeandroSSJ
 * Created on 06/09/2023
 * Protect Name Xenon
 */
public class DayCommand extends BaseCommand {

    public DayCommand(){
        super("day", "xenon.day", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        profile.setTime("DAY");
        player.setPlayerTime(0L, false);

        player.sendMessage(CC.translate(Locale.DAY_SET));
    }
}
