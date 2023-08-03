package dev.hely.core.bukkit.server;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
public class ServerManager implements ModuleLoader {

    private boolean enabled;

    private String serverName;

    private OxygenBukkit plugin;

    @Override
    public void enable() {
        this.plugin = OxygenBukkit.getInstance();

        this.serverName = plugin.getConfiguration().getConfiguration().getString("server.name");

        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void reload() {
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Server";
    }
}
