package dev.hely.core.bukkit.profile.squads.commands.subcommands.captain;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.profile.squads.invites.SquadInvite;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SquadInviteCommand extends SubCommand {
    public SquadInviteCommand() {
        super("invite", Collections.singletonList("inv"));
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
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.usage")));
            return;
        }

        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
        if (squad.getMembers().size() >= squad.getLimit()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.limit")));
            return;
        }
        if (!squad.getCaptains().contains(player.getUniqueId()) && !squad.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.not-captain")));
            return;
        }
        Player target = OxygenBukkit.getInstance().getServer().getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(Locale.COMMANDS_PLAYER_NEVER_PLAYED.replace("%player%", args[1]));
            return;
        }
        Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (targetProfile == null) {
            player.sendMessage(Locale.COMMANDS_PLAYER_NEVER_PLAYED.replace("%player%", args[1]));
            return;
        }
        if (target == player) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.self")));
            return;
        }
        if (squad.getMembers().contains(target.getUniqueId())) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.already-in").replace("%player%", target.getDisplayName())));
            return;
        }
        if (targetProfile.getSquad() != null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.has-squad")));
            return;
        }
        SquadInvite squadInvite = targetProfile.getSquadInvites().stream().filter(ci -> ci.getSquad().equalsIgnoreCase(squad.getName()) && System.currentTimeMillis() - ci.getTimestamp() <= 60000).findFirst().orElse(null);
        if (squadInvite != null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.already-invite").replace("%player%", target.getName())));
            return;
        }
        targetProfile.getSquadInvites().add(new SquadInvite(squad.getName()));

        squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.broadcast").replace("%player%", target.getName())));
        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.invited").replace("%player%", target.getName())));

        target.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.invite").replace("%squad%", squad.getName())));
        targetProfile.save();
    }
}


