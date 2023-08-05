package dev.hely.core.bukkit.tag.command.subcommand;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.core.bukkit.tag.packet.implement.TagUpdatePacket;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
public class TagSetWeightCommand extends SubCommand {

    public TagSetWeightCommand() {
        super("setweight","oxygen.tag.setweight");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(Locale.TAG_SETWEIGHT_USAGE));
            return;
        }

        Tag tag = OxygenBukkit.getInstance().getTagManager().getTag(args[1]);
        if (tag == null) {
            sender.sendMessage(CC.translate(Locale.TAG_DONT_EXISTS));
            return;
        }

        if(!this.checkNumber(sender, args[2])) return;
        int weight = Integer.parseInt(args[2]);

        tag.setWeight(weight);
        tag.save();

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new TagUpdatePacket(tag.getName(), false));
        sender.sendMessage(CC.translate(Locale.TAG_SETWEIGHT_UPDATED)
                .replace("%tag_name%", tag.getName())
                .replace("%tag_weight%", String.valueOf(weight)));
    }
}
