package net.ascend.core.bukkit.rank.rank.packet;

import com.mongodb.client.model.Filters;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankDeletePacket;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import lombok.AllArgsConstructor;
import org.bson.Document;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name Xenon
 */
@AllArgsConstructor
public class RankPacketListener implements PacketListener {

    @IncomingPacketHandler
    public void onRankUpdate(RankUpdatePacket packet) {
        String rankName = packet.getName();

        Rank rank = XenonBukkit.getInstance().getRankManager().getRank(rankName);

        if (rank == null) { // CREATE NEW RANK IN OTHERS SERVER
            XenonBukkit.getInstance().getRankManager().getRanks().add(new Rank(rankName));
            rank = XenonBukkit.getInstance().getRankManager().getRank(rankName);
        }

        Document document = XenonBukkit.getInstance().getMongoModule().getRanksCollection().find().filter(Filters.eq("name", rankName)).first();
        if (document != null) {
            rank.load();
        }
    }

    @IncomingPacketHandler
    public void onRankDelete(RankDeletePacket packet) {
        String rankName = packet.getName();

        XenonBukkit.getInstance().getRankManager().getRanks().removeIf(rank -> rank.getName().equalsIgnoreCase(rankName));
    }
}