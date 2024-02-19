package net.ascend.core.bukkit.profile.squads.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
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
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getSquad() != null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.already-in-squad")));
            return;
        }

        String name = args[1];

        if (name.length() < 2) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.invalid-name")));
            return;
        }

        if (!StringUtils.isAlpha(name)) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.only-characters")));
            return;
        }

        if (name.length() > 8) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.too-long")));
            return;
        }

        Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(name);
        if (squad != null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.already-exist")));
            return;
        }

        Squad newSquad = new Squad(name, player.getUniqueId());
        newSquad.getMembers().add(player.getUniqueId());
        profile.setSquad(name);

        newSquad.setDateCreated(System.currentTimeMillis());
        XenonBukkit.getInstance().getSquadManager().getSquads().add(newSquad);

        player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.created")));

        XenonBukkit.getInstance().getTaskUtil().runAsync(XenonBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();
    }
}


