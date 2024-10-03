package net.ascend.core.bukkit.tag.command.subcommand;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagType;
import net.ascend.core.bukkit.tag.packet.implement.TagUpdatePacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
public class TagSetCoinsCommand extends SubCommand {

    public TagSetCoinsCommand() {
        super("setcoins","xenon.tag.setcoins");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Locale.TAG_SETCOINS_USAGE));
            return;
        }

        Tag tag = XenonBukkit.getInstance().getTagManager().getTag(args[0]);
        if (tag == null) {
            sender.sendMessage(CC.translate(Locale.TAG_DONT_EXISTS));
            return;
        }

        if (!this.checkNumber(sender, args[1])) return;
        int amount = Math.abs(Integer.parseInt(args[1]));

        tag.setCoins(amount);
        tag.save();

        XenonBukkit.getInstance().getRedisModule().sendPacket(new TagUpdatePacket(tag.getName(), false));

        sender.sendMessage(CC.translate(Locale.TAG_SETCOINS_UPDATED)
                .replace("%tag_name%", tag.getName())
                .replace("%tag_coins%", String.valueOf(tag.getCoins())));
    }
}
