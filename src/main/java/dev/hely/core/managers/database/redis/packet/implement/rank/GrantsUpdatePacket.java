package dev.hely.core.managers.database.redis.packet.implement.rank;

import com.google.gson.*;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.profile.Profile;
import dev.hely.core.ranks.grant.Grants;
import dev.hely.core.ranks.gson.RankGsonType;
import dev.hely.core.utils.json.JsonBuilder;
import lombok.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class GrantsUpdatePacket extends RedisPacket {

    private UUID targetId;
    private List<Grants> updatedGrantList = new ArrayList<>();

    @Override
    public void onReceive(JsonObject data) {
        UUID uuid = UUID.fromString(data.get("uuid").getAsString());

        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            Profile playerData = plugin.getProfileManager().getProfile(player.getUniqueId());
            playerData.setGrants(Oxygen.PLAIN_GSON.fromJson(data.get("grants").getAsString(), RankGsonType.GRANT));

            playerData.loadAttachments(player);
        }
    }

    @Override
    public JsonBuilder getData() {
        return new JsonBuilder()
                .addProperty("uuid", this.targetId.toString())
                .addProperty("grants", Oxygen.PLAIN_GSON.toJson(this.updatedGrantList, RankGsonType.GRANT));
    }

    @Override
    public String getIdentifier() {
        return "GrantsUpdate";
    }
}
