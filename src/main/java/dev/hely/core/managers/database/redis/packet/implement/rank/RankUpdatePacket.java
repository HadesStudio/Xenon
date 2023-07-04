package dev.hely.core.managers.database.redis.packet.implement.rank;

import com.google.gson.*;
import com.mongodb.client.model.*;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.ranks.Ranks;
import dev.hely.core.utils.PluginUtils;
import dev.hely.core.utils.Tasks;
import dev.hely.core.utils.json.JsonBuilder;
import lombok.*;
import org.bson.*;

@AllArgsConstructor
@NoArgsConstructor
public class RankUpdatePacket extends RedisPacket {

    private Ranks rankData;

    @Override
    public void onReceive(JsonObject data) {
        Tasks.runAsync(plugin, () -> {
            Ranks rankData = plugin.getRankManager().getRank(data.get("name").getAsString());

            if (rankData == null) {
                plugin.getRankManager().getRanks().add(new Ranks(plugin, data.get("name").getAsString(), PluginUtils.randomString()));
                rankData = plugin.getRankManager().getRank(data.get("name").getAsString());
            }
            Document document = plugin.getMongoManager().getRanksCollection().find().filter(Filters.eq("name", data.get("name").getAsString())).first();

            if (document != null) {
                rankData.load(document);
            }
        });
    }

    @Override
    public JsonBuilder getData() {
        return new JsonBuilder().addProperty("name", this.rankData.getName());
    }

    @Override
    public String getIdentifier() {
        return "RankUpdate";
    }
}
