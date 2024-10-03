package net.ascend.core.bukkit.rank.rank.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.ascend.core.bukkit.XenonBukkit;

import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Xenon
 */

@Getter
@Setter
@AllArgsConstructor
public class PermissionData {

    private String permission;
    private boolean bungeecord;
    private List<String> servers;

    public boolean isGlobal() {
        return servers.isEmpty();
    }

    public boolean isAvailable() {
        if (isGlobal()) return true;
        for (String name : this.servers) {
            if (XenonBukkit.getInstance().getServerManager().getCurrentServerName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
