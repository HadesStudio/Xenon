package dev.hely.core.managers.server.global_player;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.managers.server.global_player
//   Date: Tuesday, July 04, 2023 - 5:07 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.managers.server.global_player.packets.PlayerMessagePacket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class GlobalPlayer {

    private final UUID uniqueId;
    private final String name;

    private Oxygen plugin = Oxygen.getInstance();
    private String server, address, rankName, firstJoined, lastServer, niceName, displayName, nameColor, realName;
    private List<String> permissions = new ArrayList<>();
    private List<String> addresses = new ArrayList<>();
    private boolean quited = false;

    private long lastSeen, lastActivity = -1L;

    private int rankWeight;
    private int punishPriority;

    private boolean online = false;

    public boolean hasPermission(String value) {
        List<String> permissions = new ArrayList<>(this.permissions);

        if (permissions.stream().anyMatch(permission -> permission.equalsIgnoreCase("*"))) return true;
        if (value.contains(".")) {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == '.') {
                    int t = i;
                    if (permissions.stream().anyMatch(permission -> permission.equalsIgnoreCase(value.substring(0, t) + ".*"))) {
                        return true;
                    }
                }
            }
        }

        return permissions.stream().anyMatch(permission -> permission.equalsIgnoreCase(value));
    }

    public void sendMessage(String message) {
        PlayerMessagePacket packet = new PlayerMessagePacket(this.uniqueId, message);
        packet.send();
    }
}

