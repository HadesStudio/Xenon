package net.ascend.core.bukkit.tag.command.subcommand;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.packet.implement.TagImportPacket;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
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
public class TagImportCommand extends SubCommand {

    public TagImportCommand() {
        super("import", "xenon.tag.import");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        XenonBukkit.getInstance().getTagManager().deleteTags();

        YamlConfiguration config = XenonBukkit.getInstance().getTagConfig().getConfiguration();
        config.getKeys(false).forEach(key -> {
            Tag tag = new Tag(config.getString(key + ".name"));
            tag.setPrefix(config.getString(key + ".prefix"));
            tag.setWeight(config.getInt(key + ".weight"));
            tag.save();

            XenonBukkit.getInstance().getTagManager().getTags().add(tag);
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

        sender.sendMessage(CC.translate(Locale.TAG_IMPORT_IMPORTED));
        XenonBukkit.getInstance().getTaskUtil().runLater(() -> XenonBukkit.getInstance().getRedisModule().sendPacket(new TagImportPacket()), 10L);
    }
}
