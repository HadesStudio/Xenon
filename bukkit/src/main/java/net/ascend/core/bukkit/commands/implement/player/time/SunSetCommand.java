package net.ascend.core.bukkit.commands.implement.player.time;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created By LeandroSSJ
 * Created on 06/09/2023
 * Protect Name Xenon
 */
public class SunSetCommand extends BaseCommand {

    public SunSetCommand(){
        super("sunset", "xenon.sunset", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        profile.setTime("SUNSET");
        player.setPlayerTime(12500L, false);

        player.sendMessage(CC.translate(Locale.SUNSET_SET));
    }
}
