package dev.hely.core.bungee.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.utils
//   Date: Saturday, August 05, 2023 - 2:08 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

@Getter
public class BungeeConfig {

    private final File file;
    private Configuration configuration;

    public BungeeConfig(Plugin plugin, String fileName) {
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
                    plugin.getLogger().severe(BungeeCC.translate("&c-------------------------------------"));
                    plugin.getLogger().severe((BungeeCC.translate("&6Oxygen Bungee Core - Configuration Error")));
                    plugin.getLogger().severe((BungeeCC.translate("&cThis is more than likely caused by a broken configuration file!")));
                    plugin.getLogger().severe(BungeeCC.translate("&cFile: " + fileName + ".yml"));
                    plugin.getLogger().severe((BungeeCC.translate("&cError:")));
                    ex.printStackTrace();
                    plugin.getLogger().severe((BungeeCC.translate("&c-------------------------------------")));
                }
            }

            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        }
        catch (IOException ex) {
            plugin.getLogger().severe(BungeeCC.translate("&c-------------------------------------"));
            plugin.getLogger().severe((BungeeCC.translate("&6Oxygen Bungee Core - Configuration Error")));
            plugin.getLogger().severe((BungeeCC.translate("&cThis is more than likely caused by a broken configuration file!")));
            plugin.getLogger().severe(BungeeCC.translate("&cFile: " + fileName + ".yml"));
            plugin.getLogger().severe((BungeeCC.translate("&cError:")));
            ex.printStackTrace();
            plugin.getLogger().severe((BungeeCC.translate("&c-------------------------------------")));
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

