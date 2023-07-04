package dev.hely.core.managers.database.redis.packet.implement.data;

import com.google.gson.*;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.profile.Profile;
import dev.hely.core.utils.json.JsonBuilder;
import lombok.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class PlayerPermissionUpdatePacket extends RedisPacket {

    private String sender;
    private UUID targetId;
    private String permission;
    private boolean set;
    private String server;
    private long expire;

    @Override
    public void onReceive(JsonObject data) {
        Player player = Bukkit.getPlayer(UUID.fromString(data.get("uuid").getAsString()));

        if (player != null) {
            Profile playerData = plugin.getProfileManager().getProfile(player.getUniqueId());

            if (playerData != null) {
                Object sender = plugin.getServerManager().getGlobalPlayer(data.get("sender").getAsString());

                if (sender == null) sender = Bukkit.getConsoleSender();

                plugin.getProfileManagement().setPermission(sender, playerData, data.get("permission").getAsString(), data.get("set").getAsBoolean(), false,
                        data.get("server").getAsString().equalsIgnoreCase("null") ? null : data.get("server").getAsString(),
                        data.has("expire") ? data.get("expire").getAsLong() : -1);
                playerData.loadInPermissions(null);
            }

            String message = "your permissions where updated wow omg";

            if (message.length() >= 1) {
                player.sendMessage(message);
            }
        }
    }

    @Override
    public JsonBuilder getData() {
        return new JsonBuilder().addProperty("sender", this.sender)
                .addProperty("uuid", this.targetId.toString())
                .addProperty("permission", this.permission)
                .addProperty("server", this.server == null ? "null" : this.server)
                .addProperty("set", this.set)
                .addProperty("expire", this.expire);
    }

    @Override
    public String getIdentifier() {
        return "PlayerPermissionUpdate";
    }
}
