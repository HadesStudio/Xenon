package dev.hely.core.managers.database.redis.packet.implement.data.global;

import com.google.gson.*;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.managers.server.global_player.GlobalPlayer;
import dev.hely.core.utils.json.JsonBuilder;
import lombok.*;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
public class GlobalPlayerStatusUpdatePacket extends RedisPacket {

    private String name;
    private UUID uuid;
    private boolean quited;
    private String server;

    @Override
    public void onReceive(JsonObject data) {
        if (!data.has("uuid") || data.get("uuid").isJsonNull()) return;

        this.uuid = UUID.fromString(data.get("uuid").getAsString());

        GlobalPlayer player = plugin.getServerManager().getRealGlobalPlayer(data.get("name").getAsString());

        if (!data.get("server").getAsString().equalsIgnoreCase(Oxygen.getInstance().getConfig().getConfig().getString("plugin-settings.server-name"))) {
            if (this.plugin.getProfileManagement().getQuitTasksId().containsKey(this.uuid)) {
                this.plugin.getServer().getScheduler().cancelTask(this.plugin.getProfileManagement().getQuitTasksId().get(this.uuid));
                this.plugin.getProfileManagement().getQuitTasksId().remove(this.uuid);
            }
        }

        if (player != null) {
            player.setQuited(data.has("quited") && data.get("quited").getAsBoolean());

            if (!player.isQuited()) {
                if (this.plugin.getProfileManagement().getQuitTasksId().containsKey(player.getUniqueId())) {
                    this.plugin.getServer().getScheduler().cancelTask(this.plugin.getProfileManagement().getQuitTasksId().get(player.getUniqueId()));
                    this.plugin.getProfileManagement().getQuitTasksId().remove(player.getUniqueId());
                }
            }
        }
    }

    @Override
    public JsonBuilder getData() {
        return new JsonBuilder().addProperty("name", this.name).addProperty("quited", this.quited)
                .addProperty("uuid", this.uuid.toString())
                .addProperty("server", this.server);
    }

    @Override
    public String getIdentifier() {
        return "GlobalPlayerStatsUpdate";
    }
}
