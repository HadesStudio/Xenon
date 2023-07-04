package dev.hely.core.managers.database.redis.packet;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.managers.database.redis.packet
//   Date: Tuesday, July 04, 2023 - 5:43 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.JsonObject;
import dev.hely.core.Oxygen;
import dev.hely.core.utils.json.JsonBuilder;

public abstract class RedisPacket {

    public Oxygen plugin = Oxygen.getInstance();

    public abstract void onReceive(JsonObject data) throws Exception;

    public abstract JsonBuilder getData();

    public abstract String getIdentifier();

    public void send() {
        plugin.getRedisManager().sendRequest(this, false);
    }

    public void send(boolean justHere) {
        plugin.getRedisManager().sendRequest(this, justHere);
    }
}
