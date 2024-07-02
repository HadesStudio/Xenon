package net.ascend.core.bukkit.hook.placeholder;

import lombok.Setter;
import org.bukkit.Bukkit;

public class PlaceholderHook {

    @Setter
    public static boolean ENABLED;

    public PlaceholderHook() {
        ENABLED = true;
        Bukkit.getLogger().info("[Xenon] Successfully hooked into the PlaceholderAPI.");
    }
}
