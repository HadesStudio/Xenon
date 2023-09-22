package dev.hely.core.bukkit.placeholder;

import lombok.Setter;
import org.bukkit.Bukkit;

public class PlaceholderHook {

    @Setter
    public static boolean ENABLED;

    public PlaceholderHook() {
        ENABLED = true;
        Bukkit.getLogger().info("[Oxygen] Successfully hooked into the PlaceholderAPI.");
    }
}
