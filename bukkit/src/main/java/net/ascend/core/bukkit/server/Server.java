package net.ascend.core.bukkit.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.data.server
//   Date: Monday, August 14, 2023 - 11:25 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.ascend.core.bukkit.XenonBukkit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
@RequiredArgsConstructor
public class Server {

    private String name = "";
    private long lastHeartbeat = System.currentTimeMillis();
    private long startTime = 0;
    private ServerState state = ServerState.UNKNOWN;
    private int onlinePlayers = 0;
    private int maxPlayers = 25;
    private double tps = 0D;
    private double fullTick = 0D;
    private boolean queueEnabled = false;
    private boolean queuePaused = false;
    private int queueRate = 5;
    private int playersInQueue = 0;

    public Server(String name) {
        this.name = name;
    }

    public void save() {
        Document document = new Document();
        document.put("name", name);
        document.put("state", state.toString());
        document.put("maxPlayers", maxPlayers);
        document.put("queueRate", queueRate);
        document.put("queuePaused", queuePaused);
        document.put("queueEnabled", queueEnabled);

        XenonBukkit.getInstance().getMongoModule().getServerCollection().replaceOne(Filters.eq("name", name), document, new ReplaceOptions().upsert(true));
    }

    public void load() {
        Document document = XenonBukkit.getInstance().getMongoModule().getServerCollection().find(Filters.eq("name", name)).first();

        if (document != null) {
            name = document.getString("name");
            state = ServerState.valueOf(document.getString("state"));
            maxPlayers = document.getInteger("maxPlayers");
            queueRate = document.getInteger("queueRate");
            queuePaused = document.getBoolean("queuePaused");
            queueEnabled = document.getBoolean("queueEnabled");
        }
    }

    public boolean canJoin() {
        return this.maxPlayers > this.onlinePlayers;
    }

    public int getOnlinePlayers() {
        return isOnline() ? onlinePlayers : 0;
    }

    public boolean isOnline() {
        switch (state) {
            case ONLINE:
            case WHITELISTED:
                return true;
            default:
                return false;
        }
    }
}
