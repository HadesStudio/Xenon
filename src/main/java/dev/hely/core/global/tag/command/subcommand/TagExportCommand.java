package dev.hely.core.global.tag.command.subcommand;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
public class TagExportCommand extends SubCommand {

    public TagExportCommand() {
        super("export", "oxygen.tag.export");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        YamlConfiguration config = OxygenBukkit.getInstance().getTagConfig().getConfiguration();
        config.getKeys(false).forEach(key -> config.set(key, null)); // REMOVE ALL TAGS

        OxygenBukkit.getInstance().getTagManager().getTags().forEach(tag -> {
            config.set(tag.getName() + ".name", tag.getName());
            config.set(tag.getName() + ".prefix", tag.getPrefix());
            config.set(tag.getName() + ".weight", tag.getWeight());
        });

        OxygenBukkit.getInstance().getTagConfig().save();
        OxygenBukkit.getInstance().getTagConfig().reload();

        sender.sendMessage(CC.translate(Locale.TAG_EXPORT_EXPORTED));
    }
}
