package net.ascend.core.bukkit.profile.squads.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
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
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getSquad() != null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.already-in-squad")));
            return;
        }

        Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());

        if (squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.leave.leader")));
            return;
        }

        squad.getMembers().remove(player.getUniqueId());
        squad.getCaptains().remove(player.getUniqueId());
        profile.setSquad(null);

        squad.broadcast(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.leave.broadcast").replace("%player%", player.getName())));

        squad.broadcast(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.leave.send")));

        XenonBukkit.getInstance().getTaskUtil().runAsync(XenonBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();
    }
}
