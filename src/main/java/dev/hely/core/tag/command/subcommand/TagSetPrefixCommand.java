package dev.hely.core.tag.command.subcommand;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Language;
import dev.hely.core.tag.Tag;
import dev.hely.core.tag.packet.implement.TagUpdatePacket;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
public class TagSetPrefixCommand extends SubCommand {

    public TagSetPrefixCommand() {
        super("setprefix", Collections.singletonList("setdisplayname"), "oxygen.tag.setprefix");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(Language.TAG_SETPREFIX_USAGE));
            return;
        }

        Tag tag = Oxygen.getInstance().getTagManager().getTag(args[1]);
        if (tag == null) {
            sender.sendMessage(CC.translate(Language.TAG_DONT_EXISTS));
            return;
        }

        tag.setPrefix(args[2]);
        tag.save();

        Oxygen.getInstance().getRedisModule().sendPacket(new TagUpdatePacket(tag.getName(), false));
        sender.sendMessage(CC.translate(Language.TAG_SETPREFIX_UPDATED)
                .replace("%tag_name%", tag.getName())
                .replace("%tag_prefix%", CC.translate(tag.getPrefix())));
    }
}
