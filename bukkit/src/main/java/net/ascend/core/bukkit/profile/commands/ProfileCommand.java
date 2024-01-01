package net.ascend.core.bukkit.profile.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.commands.subcommands.*;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/08/2023
 * Protect Name Xenon
 */
public class ProfileCommand extends SubCommandHandler {

    public ProfileCommand() {
        super("profile", Collections.singletonList("user"));

        this.addSubCommand(new ProfileInfoCommand());
        this.addSubCommand(new ProfileAddCurrencyCommand());
        this.addSubCommand(new ProfileAddPermissionCommand());
        this.addSubCommand(new ProfileRemovePermissionCommand());
        this.addSubCommand(new ProfileRemoveCurrencyCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-commands.main.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
                return false;
            }
        } else {
            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-commands.main.usage")) {
                sender.sendMessage(CC.translate(message));
            }
            return false;
        }
        return false;
    }
}
