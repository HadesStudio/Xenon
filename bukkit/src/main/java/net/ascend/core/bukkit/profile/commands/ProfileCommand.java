package net.ascend.core.bukkit.profile.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.commands.subcommands.*;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
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
        if (args.length == 0) {
            if (sender.hasPermission("xenon.profile")) {
                for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-command.usage")) {
                    sender.sendMessage(CC.translate(message));
                }
            } else {
                sender.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
                return true;
            }
            return true;
        }

        SubCommand sub = this.getSubCommand(args[0]);

        if (sub == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ARGUMENT_NOT_FOUND
                    .replace("%argument%", args[0])));
            return true;
        }

        if (sub.isPlayerOnly() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return true;
        }

        if (sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            return true;
        }

        sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }
}
