package dev.hely.core.ranks.permissions;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.permissions
//   Date: Tuesday, July 04, 2023 - 4:52 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RankPermission {

    private String permission;
    private List<String> servers;
    private boolean global;

    public boolean isAvailable() {
        if (global) return true;
        for (String name : this.servers) {
            if (Oxygen.getInstance().getConfig().getConfig().getString("plugin-settings.server-name").equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasServer(String source) {
        if (this.global) return true;
        return this.servers.stream().filter(server -> server.equalsIgnoreCase(source)).findFirst().orElse(null) != null;
    }
}