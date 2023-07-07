package dev.hely.core.tag.command;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Language;
import dev.hely.core.tag.command.subcommand.TagCreateCommand;
import dev.hely.core.tag.command.subcommand.TagRemoveCommand;
import dev.hely.core.tag.command.subcommand.TagSetPrefixCommand;
import dev.hely.core.tag.command.subcommand.TagSetWeightCommand;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.core.utils.command.sub.SubCommandHandler;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
public class TagCommand extends SubCommandHandler {
    public TagCommand() {
        super("tag", Collections.singletonList("tags"));

        this.addSubCommand(new TagCreateCommand());
        this.addSubCommand(new TagRemoveCommand());
        this.addSubCommand(new TagSetPrefixCommand());
        this.addSubCommand(new TagSetWeightCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                Language.TAG_USAGE.forEach(string -> sender.sendMessage(CC.translate(string)));
            }
        } else {
            Language.TAG_USAGE.forEach(string -> sender.sendMessage(CC.translate(string)));
        }
        return false;
    }
}