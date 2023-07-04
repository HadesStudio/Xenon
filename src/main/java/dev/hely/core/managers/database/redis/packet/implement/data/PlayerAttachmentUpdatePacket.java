package dev.hely.core.managers.database.redis.packet.implement.data;

import com.google.gson.*;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.profile.Profile;
import dev.hely.core.utils.PluginUtils;
import dev.hely.core.utils.Tasks;
import dev.hely.core.utils.json.JsonBuilder;

public class PlayerAttachmentUpdatePacket extends RedisPacket {

    @Override
    public void onReceive(JsonObject data) {
        Tasks.runAsync(plugin, () -> PluginUtils.getOnlinePlayers().forEach(player -> {
            Profile playerData = plugin.getProfileManager().getProfile(player.getUniqueId());

            if (playerData != null) {
                playerData.loadAttachments(player);
            }
        }));
    }

    @Override
    public JsonBuilder getData() {
        return new JsonBuilder();
    }

    @Override
    public String getIdentifier() {
        return "PlayerAttachmentUpdate";
    }
}
