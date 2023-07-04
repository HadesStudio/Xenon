package dev.hely.core.managers.database.redis.packet.implement.other;

import com.google.gson.*;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.utils.json.JsonBuilder;
import dev.hely.lib.CC;
import lombok.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class PlayerMessagePacket extends RedisPacket {

    private UUID uuid;
    private String message;

    @Override
    public void onReceive(JsonObject data) {
        Player player = Bukkit.getPlayer(UUID.fromString(data.get("uuid").getAsString()));

        if (player != null) {
            player.sendMessage(CC.translate(data.get("message").getAsString()));
        }
    }

    @Override
    public JsonBuilder getData() {
        return new JsonBuilder().addProperty("uuid", this.uuid.toString())
                .addProperty("message", this.message);
    }

    @Override
    public String getIdentifier() {
        return "PlayerMessage";
    }
}
