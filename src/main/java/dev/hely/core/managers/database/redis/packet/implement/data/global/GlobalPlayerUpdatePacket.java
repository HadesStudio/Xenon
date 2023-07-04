package dev.hely.core.managers.database.redis.packet.implement.data.global;

import com.google.gson.*;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.managers.server.global_player.GlobalPlayer;
import dev.hely.core.utils.StringUtils;
import dev.hely.core.utils.json.JsonBuilder;
import lombok.*;

import java.util.*;

@NoArgsConstructor
public class GlobalPlayerUpdatePacket extends RedisPacket {

    private JsonBuilder jsonBuilder;

    public GlobalPlayerUpdatePacket(JsonBuilder builder) {
        this.jsonBuilder = builder;
    }

    @Override
    public void onReceive(JsonObject received) {
        JsonArray players = received.get("players").getAsJsonArray();

        players.forEach(jsonElement -> {
            JsonObject data = jsonElement.getAsJsonObject();

            GlobalPlayer player = plugin.getServerManager().getOrCreateGlobalPlayer(data.get("name").getAsString(), UUID.fromString(data.get("uuid").getAsString()));

            if (player == null) return;

            player.setPermissions(StringUtils.getListFromString(data.get("permissions").getAsString()));

            player.setRankName(data.get("rank").getAsString());
            player.setNiceName(data.get("niceName").getAsString());
            player.setRankWeight(data.get("rankWeight").getAsInt());
            player.setAddress(data.get("address").getAsString());
            player.setLastSeen(data.get("lastSeen").getAsLong());
            player.setFirstJoined(data.get("firstJoined").getAsString());
            player.setLastActivity(data.get("lastActivity").getAsLong());
            player.setLastServer(data.has("lastServer") ? data.get("lastServer").getAsString() : null);
            player.setAddresses(StringUtils.getListFromString(data.get("addresses").getAsString()));
            player.setServer(data.get("server").getAsString());
            player.setPunishPriority(data.has("punishPriority") ? data.get("punishPriority").getAsInt() : 1);
            player.setQuited(data.has("quited") && data.get("quited").getAsBoolean());
            player.setDisplayName(data.has("displayName") ? data.get("displayName").getAsString() : player.getName());
            player.setRealName(data.has("realName") ? data.get("realName").getAsString() : player.getName());
            if (data.has("nameColor")) {
                player.setNameColor(data.get("nameColor").getAsString());
            } else {
                player.setNameColor(data.has("nameColor") ? data.get("nameColor").getAsString() : null);
            }
        });
    }

    @Override
    public JsonBuilder getData() {
        return this.jsonBuilder;
    }

    @Override
    public String getIdentifier() {
        return "GlobalPlayerUpdate";
    }
}
