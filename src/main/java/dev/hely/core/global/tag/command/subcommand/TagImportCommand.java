package dev.hely.core.global.tag.command.subcommand;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.global.tag.Tag;
import dev.hely.core.global.tag.packet.implement.TagImportPacket;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
public class TagImportCommand extends SubCommand {

    public TagImportCommand() {
        super("import", "oxygen.tag.import");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OxygenBukkit.getInstance().getTagManager().deleteTags();

        YamlConfiguration config = OxygenBukkit.getInstance().getTagConfig().getConfiguration();
        config.getKeys(false).forEach(key -> {
            Tag tag = new Tag(config.getString(key + ".name"));
            tag.setPrefix(config.getString(key + ".prefix"));
            tag.setWeight(config.getInt(key + ".weight"));
            tag.save();

            OxygenBukkit.getInstance().getTagManager().getTags().add(tag);
        });

        OxygenBukkit.getInstance().getTagConfig().save();
        OxygenBukkit.getInstance().getTagConfig().reload();

        sender.sendMessage(CC.translate(Locale.TAG_IMPORT_IMPORTED));
        OxygenBukkit.getInstance().getTasks().runLater(() -> OxygenBukkit.getInstance().getRedisModule().sendPacket(new TagImportPacket()), 10L);
    }
}
