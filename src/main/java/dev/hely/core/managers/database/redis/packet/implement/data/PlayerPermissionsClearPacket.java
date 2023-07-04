package dev.hely.core.managers.database.redis.packet.implement.data;

import com.google.gson.*;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.managers.server.global_player.GlobalPlayer;
import dev.hely.core.profile.Profile;
import dev.hely.core.utils.json.JsonBuilder;
import lombok.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class PlayerPermissionsClearPacket extends RedisPacket {

    private String sender;
    private UUID uuid;
    private String name;

    @Override
    public void onReceive(JsonObject data) {
        this.sender = data.get("sender").getAsString();
        this.uuid = UUID.fromString(data.get("uuid").getAsString());
        this.name = data.get("name").getAsString();

        Player player = Bukkit.getPlayer(this.uuid);

        if (player != null) {
            Profile playerData = this.plugin.getProfileManager().getProfile(player.getUniqueId());

            if (playerData != null) {
                playerData.getPermissions().clear();

                GlobalPlayer executor = this.plugin.getServerManager().getGlobalPlayer(this.sender);

                if (executor != null) {
                    player.sendMessage("you have cleared that players permissions wowwww omg");
                    executor.sendMessage("your permissions have been cleared wow omggg");
                } else {
                    player.sendMessage("you have cleared that players permissions wowwww omg");
                    Bukkit.getConsoleSender().sendMessage("your permissions have been cleared wow omggg");
                }
            }
        } else {
            GlobalPlayer executor = this.plugin.getServerManager().getGlobalPlayer(this.sender);

            if (executor != null) {
                executor.sendMessage("ok you have reset their permissions");
            } else {
                Bukkit.getConsoleSender().sendMessage("your permissions where reset by someone idk who rata de mierda");
            }
        }
    }

    @Override
    public JsonBuilder getData() {
        return new JsonBuilder().addProperty("uuid", this.uuid.toString()).addProperty("sender", this.sender)
                .addProperty("name", this.name);
    }

    @Override
    public String getIdentifier() {
        return "PlayerPermissionsClear";
    }
}
