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
public class TagRemoveCommand extends SubCommand {

    public TagRemoveCommand() {
        super("remove", Collections.singletonList("delete"), "oxygen.tag.remove");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Locale.TAG_REMOVE_USAGE));
            return;
        }

        Tag tag = OxygenBukkit.getInstance().getTagManager().getTag(args[1]);
        if (tag == null) {
            sender.sendMessage(CC.translate(Locale.TAG_DONT_EXISTS));
            return;
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new TagUpdatePacket(tag.getName(), true));
        sender.sendMessage(CC.translate(Locale.TAG_REMOVE_REMOVED).replace("%tag_name%", tag.getPrefix() == null ? tag.getName() : tag.getPrefix()));

        tag.remove();
    }
}
