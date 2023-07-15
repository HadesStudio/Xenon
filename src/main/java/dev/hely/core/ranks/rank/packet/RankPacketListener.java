package dev.hely.core.ranks.rank.packet;

import com.mongodb.client.model.Filters;
import dev.hely.core.Oxygen;
import dev.hely.core.modules.database.redis.packet.PacketListener;
import dev.hely.core.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.ranks.rank.Rank;
import dev.hely.core.ranks.rank.packet.implement.RankDeletePacket;
import dev.hely.core.ranks.rank.packet.implement.RankUpdatePacket;
import lombok.AllArgsConstructor;
import org.bson.Document;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class RankPacketListener implements PacketListener {

    @IncomingPacketHandler
    public void onRankUpdate(RankUpdatePacket packet) {
        String rankName = packet.getName();

        Rank rank = Oxygen.getInstance().getRankManager().getRank(rankName);

        if (rank == null) { // CREATE NEW RANK IN OTHERS SERVER
            Oxygen.getInstance().getRankManager().getRanks().add(new Rank(rankName));
            rank = Oxygen.getInstance().getRankManager().getRank(rankName);
        }

        Document document = Oxygen.getInstance().getMongoModule().getRanksCollection().find().filter(Filters.eq("name", rankName)).first();
        if (document != null) {
            rank.load();
        }
    }

    @IncomingPacketHandler
    public void onRankDelete(RankDeletePacket packet) {
        String rankName = packet.getName();

        Oxygen.getInstance().getRankManager().getRanks().removeIf(rank -> rank.getName().equalsIgnoreCase(rankName));
    }
}