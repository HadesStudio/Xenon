package net.ascend.core.bukkit.profile.squads.commands.subcommands.captain;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
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
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (args.length != 2) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.usage")));
            return;
        }
        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
        if (!squad.getCaptains().contains(player.getUniqueId()) && !squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.not-captain")));
            return;
        }

        OfflinePlayer offlinePlayer = XenonBukkit.getInstance().getServer().getOfflinePlayer(args[1]);
        if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer() == player) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kick-self")));
            return;
        }
        if (!squad.getMembers().contains(offlinePlayer.getUniqueId())) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.not-in")));
            return;
        }
        if ((squad.getCaptains().contains(offlinePlayer.getUniqueId()) && !squad.getLeader().equals(player.getUniqueId())) || squad.getLeader() == offlinePlayer.getUniqueId()) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kick-members")));
            return;
        }
        squad.getMembers().remove(offlinePlayer.getUniqueId());
        squad.getCaptains().remove(offlinePlayer.getUniqueId());

        if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer().isOnline()) {
            Player target = offlinePlayer.getPlayer();

            Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(target.getUniqueId());
            targetProfile.setSquad(null);

            target.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kicked")));
        }

        squad.broadcast(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.broadcast").replace("%player%", offlinePlayer.getName())));
    }
}


