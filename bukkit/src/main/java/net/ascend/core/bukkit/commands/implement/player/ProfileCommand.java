package net.ascend.core.bukkit.commands.implement.player;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.myprofile.menus.MainProfileMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ProfileCommand extends BaseCommand {

    public ProfileCommand(){
        super("myprofile", Collections.singletonList("p"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        new MainProfileMenu(profile).openMenu(player);
    }
}
