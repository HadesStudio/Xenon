package dev.hely.core.bukkit.profile.squads;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.squads.members.SquadMemberType;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Squad {
    private final String name;
    private UUID leader;
    private int limit = OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("settings.squad-limit");
    private List<UUID> members = new ArrayList<>();
    private List<UUID> captains = new ArrayList<>();
    private String password = null;
    private long dateCreated;
    private String discord = null;
    private boolean open = false;

    private OxygenBukkit plugin = OxygenBukkit.getInstance();

    public Squad(String name, UUID leader) {
        this.name = name;
        this.leader = leader;

        save();
    }

    public void save() {
        Document document = new Document();
        document.put("name", this.name);
        document.put("leader", leader.toString());
        document.put("discord", discord);
        document.put("open", open);
        Type membersType = new TypeToken<List<UUID>>() {}.getType();
        document.put("members", OxygenBukkit.PLAIN_GSON.toJson(this.members, membersType));
        Type captainsType = new TypeToken<List<UUID>>() {}.getType();
        document.put("captains", OxygenBukkit.PLAIN_GSON.toJson(this.captains, captainsType));
        document.put("password", password);
        document.put("dateCreated", dateCreated);

        plugin.getMongoModule().getSquadsCollection().replaceOne(Filters.eq("name", name), document, new UpdateOptions().upsert(true));
    }

    public void broadcast(String message) {
        members.forEach(users -> {
            Player player = (Player) Bukkit.getOfflinePlayer(users);

            if (player != null) {
                player.sendMessage(message);
            }
        });
    }

    public int getOnline() {
        int amount = 0;

        for (UUID uuid : members) {
            Player player = (Player) Bukkit.getOfflinePlayer(uuid);

            if (player != null) {
                amount = amount + 1;
            }
        }
        return amount;
    }

    public SquadMemberType getMemberRank(UUID uuid) {
        if (leader.equals(uuid)) {
            return SquadMemberType.LEADER;
        } else if (captains.contains(uuid)) {
            return SquadMemberType.CAPTAIN;
        } else if (members.contains(uuid)) {
            return SquadMemberType.MEMBER;
        }
        return null;
    }

    public String getMemberRankString(UUID uuid) {
        if (leader.equals(uuid)) {
            return "Leader";
        } else if (captains.contains(uuid)) {
            return "Captain";
        } else if (members.contains(uuid)) {
            return "Member";
        }
        return "None";
    }

    public String getDiscord() {
        if (discord == null) {
            return "Not Setup";
        } else {
            return discord;
        }
    }

    public int getMembersAmount() {
        return members.size();
    }
}
