package dev.hely.core.managers.database.redis.packet.implement.data.server;

import com.google.gson.*;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.managers.server.Server;
import dev.hely.core.utils.json.JsonBuilder;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
public class ServerUpdatePacket extends RedisPacket {

    private JsonBuilder jsonBuilder;

    @Override
    public void onReceive(JsonObject data) {
        Server serverData = plugin.getServerManager().getOrCreateServerData(data.get("name").getAsString());

        if (serverData == null) return;

        serverData.setWhitelisted(data.get("whitelisted").getAsBoolean());
        serverData.setLastTick(data.get("lastTick").getAsLong());
        serverData.setMax_players(data.get("maxPlayers").getAsInt());
        serverData.setRecentTps(new double[]{data.get("tps1").getAsDouble(), data.get("tps2").getAsDouble(), data.get("tps3").getAsDouble()});
        serverData.setMaintenance_active(data.has("maintenance") && data.get("maintenance").getAsBoolean());
    }

    @Override
    public JsonBuilder getData() {
        return this.jsonBuilder;
    }

    @Override
    public String getIdentifier() {
        return "ServerUpdate";
    }
}
