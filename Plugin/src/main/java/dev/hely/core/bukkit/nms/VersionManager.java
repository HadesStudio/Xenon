package dev.hely.core.bukkit.nms;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import lombok.Getter;
import me.clip.placeholderapi.expansion.NMSVersion;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

@Getter
public class VersionManager implements ModuleLoader {
    private NMSVersion version;

    @Override
    public void enable() {
        String packageName = "dev.hely.core.bukkit.nms.implement." + getVersion() + "NMS";

        try {
            this.version = (NMSVersion) Class.forName(packageName).getConstructors()[0].newInstance(OxygenBukkit.getInstance());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | InstantiationException | ClassNotFoundException var3) {
            OxygenBukkit.getInstance().debug_message("&7&l&m--------------------------------------------------------------------------");
            OxygenBukkit.getInstance().debug_message("Their was an error while trying to find your NMS Version for: &6" + this.getVersion());
            OxygenBukkit.getInstance().debug_message("The NMS version attempting to be detected: &6" + packageName);
            OxygenBukkit.getInstance().debug_message("This is either because their is a bug in the core, or this version is not supported!");
            OxygenBukkit.getInstance().debug_message("&7&l&m--------------------------------------------------------------------------");
        }
    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return "Version";
    }

    public String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
