package net.ascend.core.bukkit.profile.squads.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.profile.squads.commands.subcommands.captain.SquadInviteCommand;
import net.ascend.core.bukkit.profile.squads.commands.subcommands.captain.SquadKickCommand;
import net.ascend.core.bukkit.profile.squads.menu.SquadMainMenu;
import net.ascend.core.bukkit.profile.squads.menu.view.SquadInfoMenu;
import net.ascend.core.bukkit.profile.squads.commands.subcommands.*;
import net.ascend.core.bukkit.profile.squads.commands.subcommands.leader.*;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SquadCommands extends SubCommandHandler {
    public SquadCommands() {
        super(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("settings.system-name.command-name"), XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("settings.system-name.aliases"));

        this.addSubCommand(new SquadOpenCommand());
        this.addSubCommand(new SquadDemoteCommand());
        this.addSubCommand(new SquadPromoteCommand());
        this.addSubCommand(new SquadPasswordCommand());
        this.addSubCommand(new SquadKickCommand());
        this.addSubCommand(new SquadInviteCommand());
        this.addSubCommand(new SquadDisbandCommand());
        this.addSubCommand(new SquadChatCommand());
        this.addSubCommand(new SquadJoinCommand());
        this.addSubCommand(new SquadLeaveCommand());
        this.addSubCommand(new SquadCreateCommand());
        this.addSubCommand(new SquadHelpCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
                    if (profile.getSquad() == null) {
                        new SquadMainMenu(profile).openMenu(player);
                    } else {
                        Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
                        new SquadInfoMenu(profile, squad).openMenu(player);
                    }
                }
            }
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
                if (profile.getSquad() == null) {
                    new SquadMainMenu(profile).openMenu(player);
                } else {
                    Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
                    new SquadInfoMenu(profile, squad).openMenu(player);
                }
            } else {
                return false;
            }
            return false;
        }
        return false;
    }
}
