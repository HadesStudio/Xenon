package dev.hely.core.bungee.server.data.server.data;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.data.server.data
//   Date: Monday, August 14, 2023 - 11:27 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ServerPerformanceData {

    private Double tps;
    private Double fullTick;

    public ServerPerformanceData(JsonObject jsonObject) {
        this.tps = jsonObject.get("tps").getAsDouble();
        this.fullTick = jsonObject.get("fullTick").getAsDouble();
    }

    public JsonObject toJsonObject() {
        JsonObject toReturn = new JsonObject();

        toReturn.addProperty("tps", this.tps);
        toReturn.addProperty("fullTick", this.fullTick);

        return toReturn;
    }
}

