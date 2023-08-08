package dev.hely.core.bukkit.profile.commands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.commands.subcommands.ProfileAddCurrencyCommand;
import dev.hely.core.bukkit.profile.commands.subcommands.ProfileInfoCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommandHandler;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/08/2023
 * Protect Name Oxygen
 */
public class ProfileCommand extends SubCommandHandler {

    public ProfileCommand() {
        super("profile", Collections.singletonList("user"));

        this.addSubCommand(new ProfileInfoCommand());
        this.addSubCommand(new ProfileAddCurrencyCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                // ADDED USAGE
                return false;
            }
        } else {
            // ADDED USAGE
            return false;
        }
        return false;
    }
}
