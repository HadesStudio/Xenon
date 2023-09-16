package dev.hely.core.bukkit.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server
//   Date: Tuesday, September 12, 2023 - 4:28 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServerState {
    ONLINE(CC.translate("&aOnline")),
    OFFLINE(CC.translate("&cOffline (Plugin Offline)")),
    WHITELISTED(CC.translate("&d&oWhitelisted")),
    HEARTBEAT_TIMEOUT(CC.translate("&cOffline (Heartbeat Timeout)")),
    UNKNOWN(CC.translate("&cOffline (Unknown?)"));

    private final String displayName;
    private final String internalName;

    ServerState(String displayName) {
        this(displayName, displayName);
    }
}
