package dev.hely.core.managers.server.global_player.packets;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.managers.server.global_player.packets
//   Date: Tuesday, July 04, 2023 - 5:41 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.JsonObject;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.utils.json.JsonBuilder;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

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
        return new JsonBuilder().addProperty("uuid", this.uuid.toString()).addProperty("message", this.message);
    }

    @Override
    public String getIdentifier() {
        return "PlayerMessage";
    }
}
