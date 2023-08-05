package dev.hely.core.global.tag.command.subcommand;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

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

        try {
            OxygenBukkit.getInstance().getTagConfig().save();
            OxygenBukkit.getInstance().getTagConfig().reload();
        } catch (IOException | InvalidConfigurationException e) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
            bukkitCommandSender.sendMessage(CC.translate("&6Oxygen Core - Tags Configuration Error"));
            bukkitCommandSender.sendMessage(CC.translate("&c"));
            bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
            bukkitCommandSender.sendMessage(CC.translate("&cError:"));
            e.printStackTrace();
            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
        }

        sender.sendMessage(CC.translate(Locale.TAG_EXPORT_EXPORTED));
    }
}
