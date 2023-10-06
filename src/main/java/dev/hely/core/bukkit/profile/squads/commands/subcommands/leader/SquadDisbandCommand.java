package dev.hely.core.bukkit.profile.squads.commands.subcommands.leader;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SquadDisbandCommand extends SubCommand {
    public SquadDisbandCommand() {
        super("disband", Collections.singletonList("disbands"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }
        Player player = (Player)sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
        if (!squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.disband.not-leader")));
            return;
        }
        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.disband.disbanded")));
        squad.getMembers().remove(player.getUniqueId());

        profile.setSquad(null);
        profile.setSquadChat(false);
        squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.disband.broadcast")));

        squad.getMembers().forEach(u -> {
            Player member = OxygenBukkit.getInstance().getServer().getPlayer(u);

            if (member != null) {
                Profile memberProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(member.getUniqueId());
                memberProfile.setSquad(null);
                memberProfile.setSquadChat(false);
            }
        });

        OxygenBukkit.getInstance().getSquadManager().getSquads().remove(squad);
    }
}


