package dev.hely.core.server;

import dev.hely.core.Oxygen;
import dev.hely.core.modules.ModuleLoader;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name Oxygen
 */
@Getter
public class ServerManager implements ModuleLoader {

    private boolean enabled;

    private String serverName;

    private Oxygen plugin;

    @Override
    public void enable() {
        this.plugin = Oxygen.getInstance();

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
