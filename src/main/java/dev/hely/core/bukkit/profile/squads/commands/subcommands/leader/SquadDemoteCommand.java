package dev.hely.core.bukkit.profile.squads.commands.subcommands.leader;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SquadDemoteCommand extends SubCommand {
    public SquadDemoteCommand() {
        super("demote", Collections.singletonList("derank"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }
        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (args.length != 2) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.usage")));
            return;
        }
        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
        if (!squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.not-leader")));
            return;
        }
        OfflinePlayer offlinePlayer = OxygenBukkit.getInstance().getServer().getOfflinePlayer(args[1]);
        if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer() == player) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.demote-self")));
            return;
        }
        if (!squad.getMembers().contains(offlinePlayer.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.not-in")));
            return;
        }

        if (!squad.getCaptains().contains(offlinePlayer.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.not-captain")));
            return;
        }

        squad.getCaptains().remove(offlinePlayer.getUniqueId());
        squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.broadcast").replace("%player%", offlinePlayer.getName())));
        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.promote")));
    }
}


