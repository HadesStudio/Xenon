package net.ascend.core.bukkit.commands.implement.player.message;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ToggleSoundsCommand extends BaseCommand {

    public ToggleSoundsCommand(){
        super("togglesounds", Arrays.asList("messagesounds", "sounds", "togglemessagesounds"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }


        profile.getSettings().setSounds(!profile.getSettings().isSounds());

        if (profile.getSettings().isSounds()) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("togglesounds-command.enabled")));
        } else {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("togglesounds-command.disabled")));
        }
    }
}

