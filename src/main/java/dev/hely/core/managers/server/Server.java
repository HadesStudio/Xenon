package dev.hely.core.managers.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.managers.server
//   Date: Tuesday, July 04, 2023 - 5:09 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.managers.server.global_player.GlobalPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class Server {
    private final String server_name;
    private long lastTick;
    private boolean whitelisted, maintenance_active;
    private int max_players;
    private double[] recentTps = new double[]{20.0, 20.0, 20.0};

    public List<GlobalPlayer> getPlayers() {
        return Oxygen.getInstance().getServerManager().getGlobalPlayers().stream().filter(globalPlayer -> globalPlayer.getServer().equalsIgnoreCase(server_name)).collect(Collectors.toList());
    }
}
