package dev.hely.core.bukkit.profile.squads.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SquadCreateCommand extends SubCommand {
    public SquadCreateCommand() {
        super("create", Collections.singletonList("new"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }
        Player player = (Player)sender;

        if (args.length != 2) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.usage")));
            return;
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getSquad() != null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.already-in-squad")));
            return;
        }

        String name = args[1];

        if (name.length() < 2) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.invalid-name")));
            return;
        }

        if (!StringUtils.isAlpha(name)) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.only-characters")));
            return;
        }

        if (name.length() > 8) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.too-long")));
            return;
        }

        Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(name);
        if (squad != null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.already-exist")));
            return;
        }

        Squad newSquad = new Squad(name, player.getUniqueId());
        newSquad.getMembers().add(player.getUniqueId());
        OxygenBukkit.getInstance().debug_message("&4" + profile.getName() + " OLD Squad - &6" + profile.getSquad());
        profile.setSquad(name);
        OxygenBukkit.getInstance().debug_message("&4" + profile.getName() + " NEW Squad - &6" + profile.getSquad());

        newSquad.setDateCreated(System.currentTimeMillis());
        OxygenBukkit.getInstance().getSquadManager().getSquads().add(newSquad);

        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.created")));

        OxygenBukkit.getInstance().getTasks().runAsync(OxygenBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();
        OxygenBukkit.getInstance().debug_message("&4" + profile.getName() + " NEW Squad (After Saving) - &6" + profile.getSquad());
    }
}


