package dev.hely.core.bukkit.profile.squads.commands.subcommands.captain;

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

public class SquadKickCommand extends SubCommand {
    public SquadKickCommand() {
        super("kick", Collections.singletonList("k"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }
        Player player = (Player)sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (args.length != 2) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.usage")));
            return;
        }
        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
        if (!squad.getCaptains().contains(player.getUniqueId()) && !squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.not-captain")));
            return;
        }

        OfflinePlayer offlinePlayer = OxygenBukkit.getInstance().getServer().getOfflinePlayer(args[1]);
        if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer() == player) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kick-self")));
            return;
        }
        if (!squad.getMembers().contains(offlinePlayer.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.not-in")));
            return;
        }
        if ((squad.getCaptains().contains(offlinePlayer.getUniqueId()) && !squad.getLeader().equals(player.getUniqueId())) || squad.getLeader() == offlinePlayer.getUniqueId()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kick-members")));
            return;
        }
        squad.getMembers().remove(offlinePlayer.getUniqueId());
        squad.getCaptains().remove(offlinePlayer.getUniqueId());

        if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer().isOnline()) {
            Player target = offlinePlayer.getPlayer();

            Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(target.getUniqueId());
            targetProfile.setSquad(null);

            target.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kicked")));
        }

        squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.broadcast").replace("%player%", offlinePlayer.getName())));
    }
}


