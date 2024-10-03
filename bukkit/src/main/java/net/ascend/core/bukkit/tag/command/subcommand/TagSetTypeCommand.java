package net.ascend.core.bukkit.tag.command.subcommand;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagType;
import net.ascend.core.bukkit.tag.packet.implement.TagUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
public class TagSetTypeCommand extends SubCommand {

    public TagSetTypeCommand() {
        super("settype","xenon.tag.settype");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Locale.TAG_SETTYPE_USAGE));
            return;
        }

        Tag tag = XenonBukkit.getInstance().getTagManager().getTag(args[0]);
        if (tag == null) {
            sender.sendMessage(CC.translate(Locale.TAG_DONT_EXISTS));
            return;
        }

        TagType tagType;
        try {
            tagType = TagType.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage(Locale.TAG_SETTYPE_INVALID_TYPE);
            return;
        }

        tag.setType(tagType);
        tag.save();

        XenonBukkit.getInstance().getRedisModule().sendPacket(new TagUpdatePacket(tag.getName(), false));

        sender.sendMessage(CC.translate(Locale.TAG_SETTYPE_UPDATED)
                .replace("%tag_name%", tag.getName())
                .replace("%tag_type%", CC.capitalize(tag.getType().name())));
    }
}
