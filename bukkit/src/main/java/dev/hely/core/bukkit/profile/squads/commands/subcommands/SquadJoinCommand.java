package dev.hely.core.bukkit.profile.squads.commands.subcommands;

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

public class SquadJoinCommand extends SubCommand {
    public SquadJoinCommand() {
        super("join", Collections.singletonList("l"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }
        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (args.length < 2) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.usage")));
            return;
        }
        String name = args[1];
        SquadInvite squadInvite = getInvite(player, name);

        if (profile.getSquad() != null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.already-in-squad")));
            return;
        }
        if (squadInvite == null && (getSquad(name) == null || getSquad(name).getPassword() == null)) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.not-invited")));
            return;
        }
        if (squadInvite == null && getSquad(name).getPassword() != null && args.length < 3) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.password")));
            return;
        }
        Squad squad = getSquad(name);
        if (squad.getPassword() != null && args.length >= 3 && squadInvite == null) {
            String password = args[2];

            if (!squad.getPassword().equalsIgnoreCase(password)) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.invalid-password")));
                return;
            }
        }

        profile.setSquad(name);
        squad.getMembers().add(player.getUniqueId());

        if (squadInvite != null) {
            profile.getSquadInvites().remove(squadInvite);
        }

        squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.broadcast").replace("%player%", player.getName())));
        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.joined")));

        OxygenBukkit.getInstance().getTasks().runAsync(OxygenBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();
    }


    private SquadInvite getInvite(Player player, String name) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        return profile.getSquadInvites().stream().filter(ci -> ci.getSquad().equalsIgnoreCase(name) && System.currentTimeMillis() - ci.getTimestamp() <= 60000 && !ci.isCancelled()).findFirst().orElse(null);
    }

    private Squad getSquad(String name) {
        return OxygenBukkit.getInstance().getSquadManager().getSquads().stream().filter(c -> c.getName().equalsIgnoreCase(name) || c.getMembers().contains(OxygenBukkit.getInstance().getServer().getOfflinePlayer(name).getUniqueId())).findFirst().orElse(null);
    }
}
