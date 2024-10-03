package net.ascend.core.bukkit.commands.implement.player.message;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ToggleMessagesCommand extends BaseCommand {

    public ToggleMessagesCommand(){
        super("togglepm", Arrays.asList("togglemessages", "tpm"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        profile.getSettings().setPrivateMessages(!profile.getSettings().isPrivateMessages());

        if (profile.getSettings().isPrivateMessages()) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("togglepm-command.enabled")));
        } else {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("togglepm-command.disabled")));
        }
    }
}

