package dev.hely.core.tag.command.subcommand;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Language;
import dev.hely.core.tag.Tag;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
public class TagRemoveCommand extends SubCommand {

    public TagRemoveCommand() {
        super("remove", Collections.singletonList("delete"), "oxygen.tag.remove");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Language.TAG_REMOVE_USAGE));
            return;
        }

        Tag tag = Oxygen.getInstance().getTagManager().getTag(args[1]);
        if (tag == null) {
            sender.sendMessage(CC.translate(Language.TAG_DONT_EXISTS));
            return;
        }

        Oxygen.getInstance().getRedisModule().write("tag;update;remove;" + tag.getName());

        sender.sendMessage(CC.translate(Language.TAG_REMOVE_REMOVED).replace("%tag_name%", tag.getPrefix() == null ? tag.getName() : tag.getPrefix()));

        tag.remove();
    }
}
