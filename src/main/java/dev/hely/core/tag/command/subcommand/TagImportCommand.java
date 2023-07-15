package dev.hely.core.tag.command.subcommand;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.tag.Tag;
import dev.hely.core.tag.packet.implement.TagImportPacket;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
public class TagImportCommand extends SubCommand {

    public TagImportCommand() {
        super("import", "oxygen.tag.import");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Oxygen.getInstance().getTagManager().deleteTags();

        YamlConfiguration config = Oxygen.getInstance().getTagConfig().getConfiguration();
        config.getKeys(false).forEach(key -> {
            Tag tag = new Tag(config.getString(key + ".name"));
            tag.setPrefix(config.getString(key + ".prefix"));
            tag.setWeight(config.getInt(key + ".weight"));
            tag.save();

            Oxygen.getInstance().getTagManager().getTags().add(tag);
        });

        Oxygen.getInstance().getTagConfig().save();
        Oxygen.getInstance().getTagConfig().reload();

        sender.sendMessage(CC.translate(Locale.TAG_IMPORT_IMPORTED));
        Oxygen.getInstance().getTasks().runLater(() -> Oxygen.getInstance().getRedisModule().sendPacket(new TagImportPacket()), 10L);
    }
}
