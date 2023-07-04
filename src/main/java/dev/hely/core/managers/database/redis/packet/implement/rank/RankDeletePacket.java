package dev.hely.core.managers.database.redis.packet.implement.rank;

import com.google.gson.*;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.utils.json.JsonBuilder;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class RankDeletePacket extends RedisPacket {

    private String name;

    @Override
    public void onReceive(JsonObject data) {
        plugin.getRankManager().getRanks().removeIf(rank -> rank.getName().equalsIgnoreCase(data.get("name").getAsString()));
    }

    @Override
    public JsonBuilder getData() {
        return new JsonBuilder().addProperty("name", this.name);
    }

    @Override
    public String getIdentifier() {
        return "RankDelete";
    }
}
