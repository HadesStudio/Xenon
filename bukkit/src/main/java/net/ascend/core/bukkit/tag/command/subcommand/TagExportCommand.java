package net.ascend.core.bukkit.tag.command.subcommand;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
public class TagExportCommand extends SubCommand {

    public TagExportCommand() {
        super("export", "xenon.tag.export");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        YamlConfiguration config = XenonBukkit.getInstance().getTagConfig().getConfiguration();
        config.getKeys(false).forEach(key -> config.set(key, null)); // REMOVE ALL TAGS

        XenonBukkit.getInstance().getTagManager().getTags().forEach(tag -> {
            config.set(tag.getName() + ".name", tag.getName());
            config.set(tag.getName() + ".prefix", tag.getPrefix());
            config.set(tag.getName() + ".weight", tag.getWeight());
        });

        try {
            XenonBukkit.getInstance().getTagConfig().save();
            XenonBukkit.getInstance().getTagConfig().reload();
        } catch (IOException | InvalidConfigurationException e) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
            bukkitCommandSender.sendMessage(CC.translate("&cXenon - Tags Configuration Error"));
            bukkitCommandSender.sendMessage(CC.translate("&c"));
            bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
            bukkitCommandSender.sendMessage(CC.translate("&cError:"));
            e.printStackTrace();
            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
        }

        sender.sendMessage(CC.translate(Locale.TAG_EXPORT_EXPORTED));
    }
}
