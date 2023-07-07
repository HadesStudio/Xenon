package dev.hely.core.tag.command.subcommand;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Language;
import dev.hely.core.tag.Tag;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import dev.hely.lib.JavaUtil;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
public class TagCreateCommand extends SubCommand {

    public TagCreateCommand() {
        super("create", Collections.singletonList("new"), "oxygen.tag.create");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Language.TAG_CREATE_USAGE));
            return;
        }

        Tag tag = Oxygen.getInstance().getTagManager().getTag(args[1]);
        if (tag != null) {
            sender.sendMessage(CC.translate(Language.TAG_CREATE_ALREADY_EXIST));
            return;
        }

        tag = new Tag(args[1]);
        tag.save();

        Oxygen.getInstance().getRedisModule().write("tag;update;load;" + tag.getName());

        sender.sendMessage(CC.translate(Language.TAG_CREATE_CREATED).replace("%tag_name%", args[1]));
    }
}
