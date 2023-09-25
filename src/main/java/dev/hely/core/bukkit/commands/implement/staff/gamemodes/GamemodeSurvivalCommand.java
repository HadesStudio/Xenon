package dev.hely.core.bukkit.commands.implement.staff.gamemodes;

import dev.hely.core.bukkit.utils.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GamemodeSurvivalCommand extends BaseCommand {
    public GamemodeSurvivalCommand() {
        super("gms", Collections.singletonList("gm0"), "oxygen.command.gamemode", true);
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
