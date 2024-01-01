package net.ascend.core.bukkit.profile.squads.commands.subcommands.leader;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SquadPasswordCommand extends SubCommand {
    public SquadPasswordCommand() {
        super("password", Collections.singletonList("pw"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }
        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (args.length != 2) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.password.usage")));
            return;
        }
        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
        if (!squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.password.not-leader")));
            return;
        }

        String password = args[1];
        if (password.equalsIgnoreCase("none")) {
            squad.setPassword(null);
        } else {
            squad.setPassword(password);
        }

        player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.password.setup").replace("%password%", password)));
    }
}


