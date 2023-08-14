package dev.hely.core.bukkit.ranks.rank.permission;

import dev.hely.core.bukkit.OxygenBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name OxygenGlobal
 */

@Getter
@Setter
@AllArgsConstructor
public class PermissionData {

    private String permission;
    private boolean global;
    private List<String> servers;

    public boolean isActive() {
        if (global) return true;

        for (String server : this.servers) {
            if (OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name").equalsIgnoreCase(server)) return true;
        }

        return false;
    }

    public boolean getServer(String source) {
        if (this.global) return true;

        return this.servers.stream().filter(server -> server.equalsIgnoreCase(source)).findFirst().orElse(null) != null;
    }
}
