package net.ascend.core.bukkit.tag.command.subcommand;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.packet.implement.TagUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
public class TagCreateCommand extends SubCommand {

    public TagCreateCommand() {
        super("create", Collections.singletonList("new"), "xenon.tag.create");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Locale.TAG_CREATE_USAGE));
            return;
        }

        Tag tag = XenonBukkit.getInstance().getTagManager().getTag(args[1]);
        if (tag != null) {
            sender.sendMessage(CC.translate(Locale.TAG_CREATE_ALREADY_EXIST));
            return;
        }

        tag = new Tag(args[1]);
        tag.save();

        XenonBukkit.getInstance().getRedisModule().sendPacket(new TagUpdatePacket(tag.getName(), false));
        sender.sendMessage(CC.translate(Locale.TAG_CREATE_CREATED).replace("%tag_name%", args[1]));
    }
}
