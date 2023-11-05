package dev.hely.core.bukkit.tag.command.subcommand;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.core.bukkit.tag.packet.implement.TagImportPacket;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
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

        sender.sendMessage(CC.translate(Locale.TAG_IMPORT_IMPORTED));
        OxygenBukkit.getInstance().getTasks().runLater(() -> OxygenBukkit.getInstance().getRedisModule().sendPacket(new TagImportPacket()), 10L);
    }
}
