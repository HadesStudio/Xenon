package net.ascend.core.bukkit.commands.implement.player;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Date;

public class SeenCommand extends BaseCommand {

    public SeenCommand(){
        super("seen", Collections.singletonList("lastseen"),"xenon.seen");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("seen-command.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("seen-command.message")
                .replace("%player%", profile.getName())
                .replace("%time%", TimeUtil.formatIntoCalendarString(new Date(profile.getLastSeen())))
                .replace("%server%", profile.getLastServer()))
        );
    }
}
