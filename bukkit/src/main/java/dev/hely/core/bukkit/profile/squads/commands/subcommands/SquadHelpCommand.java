package dev.hely.core.bukkit.profile.squads.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SquadHelpCommand extends SubCommand {
    public SquadHelpCommand() {
        super("info", Collections.singletonList("help"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("commands.help.usage")) {
            sender.sendMessage(CC.translate(message));
        }
    }
}


