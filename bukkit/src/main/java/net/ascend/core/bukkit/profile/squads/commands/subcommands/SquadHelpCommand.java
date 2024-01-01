package net.ascend.core.bukkit.profile.squads.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SquadHelpCommand extends SubCommand {
    public SquadHelpCommand() {
        super("info", Collections.singletonList("help"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("commands.help.usage")) {
            sender.sendMessage(CC.translate(message));
        }
    }
}


