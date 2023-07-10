package dev.hely.core.profile.profilesystem.impl.serverstats.config;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.impl.serverstats.config
//   Date: Monday, July 10, 2023 - 12:00 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class StatsConfig {
    public static boolean SERVER_1_ENABLED;
    public static String SERVER_1_DISPLAY_NAME;
    public static Material SERVER_1_DISPLAY_MATERIAL;
    public static boolean SERVER_1_USE_HCF_CORE;
    public static int SERVER_1_INVENTORY_SLOT;
    public static boolean SERVER_2_ENABLED;
    public static String SERVER_2_DISPLAY_NAME;
    public static Material SERVER_2_DISPLAY_MATERIAL;
    public static boolean SERVER_2_USE_HCF_CORE;
    public static int SERVER_2_INVENTORY_SLOT;
    public static boolean SERVER_3_ENABLED;
    public static String SERVER_3_DISPLAY_NAME;
    public static Material SERVER_3_DISPLAY_MATERIAL;
    public static boolean SERVER_3_USE_HCF_CORE;
    public static int SERVER_3_INVENTORY_SLOT;
    public static boolean SERVER_4_ENABLED;
    public static String SERVER_4_DISPLAY_NAME;
    public static Material SERVER_4_DISPLAY_MATERIAL;
    public static boolean SERVER_4_USE_HCF_CORE;
    public static int SERVER_4_INVENTORY_SLOT;

    public StatsConfig() {
        YamlConfiguration config = Oxygen.getInstance().getConfiguration().getConfig();

        SERVER_1_ENABLED = config.getBoolean("statistics-settings.server-1.enabled");
        SERVER_1_DISPLAY_NAME = config.getString("statistics-settings.server-1.display-name");
        SERVER_1_DISPLAY_MATERIAL = Material.valueOf(config.getString("statistics-settings.server-1.material-display"));
        SERVER_1_USE_HCF_CORE = config.getBoolean("statistics-settings.server-1.use-hcf-core");
        SERVER_1_INVENTORY_SLOT = config.getInt("statistics-settings.server-1.inventory-slot");
        SERVER_2_ENABLED = config.getBoolean("statistics-settings.server-2.enabled");
        SERVER_2_DISPLAY_NAME = config.getString("statistics-settings.server-2.display-name");
        SERVER_2_DISPLAY_MATERIAL = Material.valueOf(config.getString("statistics-settings.server-2.material-display"));
        SERVER_2_USE_HCF_CORE = config.getBoolean("statistics-settings.server-2.use-hcf-core");
        SERVER_2_INVENTORY_SLOT = config.getInt("statistics-settings.server-2.inventory-slot");
        SERVER_3_ENABLED = config.getBoolean("statistics-settings.server-3.enabled");
        SERVER_3_DISPLAY_NAME = config.getString("statistics-settings.server-3.display-name");
        SERVER_3_DISPLAY_MATERIAL = Material.valueOf(config.getString("statistics-settings.server-3.material-display"));
        SERVER_3_USE_HCF_CORE = config.getBoolean("statistics-settings.server-3.use-hcf-core");
        SERVER_3_INVENTORY_SLOT = config.getInt("statistics-settings.server-3.inventory-slot");
        SERVER_4_ENABLED = config.getBoolean("statistics-settings.server-4.enabled");
        SERVER_4_DISPLAY_NAME = config.getString("statistics-settings.server-4.display-name");
        SERVER_4_DISPLAY_MATERIAL = Material.valueOf(config.getString("statistics-settings.server-4.material-display"));
        SERVER_4_USE_HCF_CORE = config.getBoolean("statistics-settings.server-4.use-hcf-core");
        SERVER_4_INVENTORY_SLOT = config.getInt("statistics-settings.server-4.inventory-slot");
    }
}
