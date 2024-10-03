package net.ascend.core.bungee.util;

import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

@Getter
public class Config {

    private final File file;
    private Configuration configuration;

    public Config(Plugin plugin, String fileName) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.file = new File(plugin.getDataFolder(), fileName + ".yml");

        try {
            if (!file.exists()) {
                file.createNewFile();

                InputStream inputStream = plugin.getResourceAsStream(fileName + ".yml");

                try {
                    OutputStream outputStream = new FileOutputStream(file);
                    ByteStreams.copy(inputStream, outputStream);
                }
                catch (IOException ex) {
                    plugin.getLogger().severe(CC.translate("&c-------------------------------------"));
                    plugin.getLogger().severe((CC.translate("&cXnon - Configuration Error")));
                    plugin.getLogger().severe((CC.translate("&cThis is more than likely caused by a broken configuration file!")));
                    plugin.getLogger().severe(CC.translate("&cFile: " + fileName + ".yml"));
                    plugin.getLogger().severe((CC.translate("&cError:")));
                    ex.printStackTrace();
                    plugin.getLogger().severe((CC.translate("&c-------------------------------------")));
                }
            }

            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        }
        catch (IOException ex) {
            plugin.getLogger().severe(CC.translate("&c-------------------------------------"));
            plugin.getLogger().severe((CC.translate("&cXenon - Configuration Error")));
            plugin.getLogger().severe((CC.translate("&cThis is more than likely caused by a broken configuration file!")));
            plugin.getLogger().severe(CC.translate("&cFile: " + fileName + ".yml"));
            plugin.getLogger().severe((CC.translate("&cError:")));
            ex.printStackTrace();
            plugin.getLogger().severe((CC.translate("&c-------------------------------------")));
            plugin.getLogger().severe("Failed to create file " + fileName + ".yml");
        }
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload() {
        try {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

