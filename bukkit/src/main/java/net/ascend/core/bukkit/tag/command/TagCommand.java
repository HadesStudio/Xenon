package net.ascend.core.bukkit.tag.command;

import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.tag.command.subcommand.*;
import net.ascend.core.bukkit.tag.menu.TagMenu;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
public class TagCommand extends SubCommandHandler {

    public TagCommand() {
        super("tag", Collections.singletonList("tags"));

        this.addSubCommand(new TagCreateCommand());
        this.addSubCommand(new TagRemoveCommand());
        this.addSubCommand(new TagSetTypeCommand());
        this.addSubCommand(new TagSetPrefixCommand());
        this.addSubCommand(new TagSetWeightCommand());
        this.addSubCommand(new TagSetCoinsCommand());
        this.addSubCommand(new TagExportCommand());
        this.addSubCommand(new TagImportCommand());
    }


    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("xenon.tag")) {
                for (String msg : Locale.TAG_USAGE) {
                    sender.sendMessage(CC.translate(msg));
                }

                if (sender instanceof Player) {
                    new TagMenu().openMenu((Player) sender);
                }
            } else {
                new TagMenu().openMenu((Player) sender);
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