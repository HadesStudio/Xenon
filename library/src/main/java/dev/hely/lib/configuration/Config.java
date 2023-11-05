package dev.hely.lib.configuration;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.utils
//   Date: Saturday, August 05, 2023 - 2:41 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
public class Config {

    private final File file;
    private YamlConfiguration configuration;

    public Config(JavaPlugin plugin, String name) throws IOException, InvalidConfigurationException {
        file = new File(plugin.getDataFolder(), name + ".yml");
        if (!file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.getFile());
    }

    public void save() throws IOException, InvalidConfigurationException {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() throws IOException, InvalidConfigurationException {
        this.configuration = YamlConfiguration.loadConfiguration(this.getFile());
    }

    public File getFile() {
        return this.file;
    }

    public YamlConfiguration getConfig() {
        return this.configuration;
    }

}
