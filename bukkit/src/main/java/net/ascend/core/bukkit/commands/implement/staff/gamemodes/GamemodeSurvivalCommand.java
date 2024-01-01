package net.ascend.core.bukkit.commands.implement.staff.gamemodes;

import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GamemodeSurvivalCommand extends BaseCommand {
    public GamemodeSurvivalCommand() {
        super("gms", Collections.singletonList("gm0"), "xenon.command.gamemode", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.performCommand("gamemode s");
        } else {
            player.performCommand("gamemode " + args[0] + " s");
        }
    }
}
