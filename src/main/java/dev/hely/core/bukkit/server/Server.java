package dev.hely.core.bukkit.server;

import lombok.*;

/**
 * Created By LeandroSSJ
 * Created on 06/08/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Server {

    private final String name;
    private boolean status;

    private String version;
    private long startedAt;
    private double tps;
    private boolean whitelisted;
    private int maxPlayers;
    private int onlinePlayers;

}
