package dev.hely.core.tag.command.subcommand;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
public class TagExportCommand extends SubCommand {

    public TagExportCommand() {
        super("export", "oxygen.tag.export");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        YamlConfiguration config = Oxygen.getInstance().getTagConfig().getConfiguration();
        config.getKeys(false).forEach(key -> config.set(key, null)); // REMOVE ALL TAGS

        Oxygen.getInstance().getTagManager().getTags().forEach(tag -> {
            config.set(tag.getName() + ".name", tag.getName());
            config.set(tag.getName() + ".prefix", tag.getPrefix());
            config.set(tag.getName() + ".weight", tag.getWeight());
        });

        Oxygen.getInstance().getTagConfig().save();
        Oxygen.getInstance().getTagConfig().reload();

        sender.sendMessage(CC.translate(Locale.TAG_EXPORT_EXPORTED));
    }
}
