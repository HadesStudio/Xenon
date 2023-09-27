package dev.hely.core.bukkit.tag.command;

import dev.hely.core.bukkit.tag.command.subcommand.*;
import dev.hely.core.bukkit.tag.menu.TagMenu;
import dev.hely.core.bukkit.tag.menu.menu.CustomMenu;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
public class TagCommand extends SubCommandHandler {
    public TagCommand() {
        super("tag", Collections.singletonList("tags"));

        this.addSubCommand(new TagCreateCommand());
        this.addSubCommand(new TagRemoveCommand());
        this.addSubCommand(new TagSetPrefixCommand());
        this.addSubCommand(new TagSetWeightCommand());
        this.addSubCommand(new TagExportCommand());
        this.addSubCommand(new TagImportCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                new CustomMenu().openMenu((Player) sender);
            }
        } else {
            new CustomMenu().openMenu((Player) sender);
        }
        return false;
    }
}