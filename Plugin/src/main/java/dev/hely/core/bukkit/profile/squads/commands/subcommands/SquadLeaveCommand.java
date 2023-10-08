package dev.hely.core.bukkit.profile.squads.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SquadLeaveCommand extends SubCommand {
    public SquadLeaveCommand() {
        super("leave", Collections.singletonList("l"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }
        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getSquad() != null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.already-in-squad")));
            return;
        }

        Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());

        if (squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.leave.leader")));
            return;
        }

        squad.getMembers().remove(player.getUniqueId());
        squad.getCaptains().remove(player.getUniqueId());
        profile.setSquad(null);

        squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.leave.broadcast").replace("%player%", player.getDisplayName())));

        squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.leave.send")));

        OxygenBukkit.getInstance().getTasks().runAsync(OxygenBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();
    }
}
