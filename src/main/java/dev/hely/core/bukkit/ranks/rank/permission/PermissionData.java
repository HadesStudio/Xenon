package dev.hely.core.bukkit.ranks.rank.permission;

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
    private boolean bungeecord = false; // We want this false by default puta ;d
    private List<String> servers;

    public boolean isGlobal() {
        return servers.isEmpty();
    }
}
