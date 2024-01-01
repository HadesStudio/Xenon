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
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
        if (!squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.disband.not-leader")));
            return;
        }
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.disband.disbanded")));
        squad.getMembers().remove(player.getUniqueId());

        profile.setSquad(null);
        profile.setSquadChat(false);
        squad.broadcast(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.disband.broadcast")));

        squad.getMembers().forEach(u -> {
            Player member = XenonBukkit.getInstance().getServer().getPlayer(u);

            if (member != null) {
                Profile memberProfile = XenonBukkit.getInstance().getProfileManager().getProfile(member.getUniqueId());
                memberProfile.setSquad(null);
                memberProfile.setSquadChat(false);
            }
        });

        XenonBukkit.getInstance().getSquadManager().getSquads().remove(squad);
    }
}


