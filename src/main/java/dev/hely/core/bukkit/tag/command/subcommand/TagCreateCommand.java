package dev.hely.core.bukkit.tag.command.subcommand;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.core.bukkit.tag.packet.implement.TagUpdatePacket;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
public class TagCreateCommand extends SubCommand {

    public TagCreateCommand() {
        super("create", Collections.singletonList("new"), "oxygen.tag.create");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Locale.TAG_CREATE_USAGE));
            return;
        }

        Tag tag = OxygenBukkit.getInstance().getTagManager().getTag(args[1]);
        if (tag != null) {
            sender.sendMessage(CC.translate(Locale.TAG_CREATE_ALREADY_EXIST));
            return;
        }

        tag = new Tag(args[1]);
        tag.save();

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new TagUpdatePacket(tag.getName(), false));
        sender.sendMessage(CC.translate(Locale.TAG_CREATE_CREATED).replace("%tag_name%", args[1]));
    }
}
