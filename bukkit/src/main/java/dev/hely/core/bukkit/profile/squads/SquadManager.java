package dev.hely.core.bukkit.profile.squads;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.lib.util.CC;
import lombok.Getter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class SquadManager implements ModuleLoader {
    private final List<Squad> squads = new ArrayList<>();

    @Override
    public void enable() {
        loadAll();
    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return "Squad";
    }


    private void loadAll() {
        MongoCollection<Document> collection = OxygenBukkit.getInstance().getMongoModule().getSquadsCollection();

        for (Document document : collection.find()) {
            Squad squad = new Squad(document.getString("name"), UUID.fromString(document.getString("leader")));

            Type membersType = new TypeToken<List<UUID>>() {}.getType();
            squad.setMembers(OxygenBukkit.PLAIN_GSON.fromJson(document.getString("members"), membersType));
            squad.getMembers().add(UUID.fromString(document.getString("leader")));
            Type captainsType = new TypeToken<List<UUID>>() {}.getType();
            squad.setCaptains(OxygenBukkit.PLAIN_GSON.fromJson(document.getString("captains"), captainsType));
            squad.setDateCreated(document.getLong("dateCreated"));
            squad.setDiscord(document.getString("discord"));
            if (document.containsKey("password")) {
                squad.setPassword(document.getString("password"));
            }
            squad.setOpen(document.getBoolean("open"));

            for (UUID uuid : squad.getMembers()) {
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(uuid);
                profile.setSquad(squad.getName());
                profile.save();
            }

            squads.add(squad);
        }
    }

    public void saveAll() {
        MongoCollection<Document> collection = OxygenBukkit.getInstance().getMongoModule().getSquadsCollection();
        collection.deleteMany(new Document());

        for (Squad squad : squads) {
            squad.getMembers().remove(squad.getLeader()); // We do this so they don't duplicate.. wtf xd simple fix

            Document document = new Document();
            document.put("name", squad.getName());
            document.put("leader", squad.getLeader().toString());
            document.put("discord", squad.getDiscord());
            document.put("open", squad.isOpen());
            Type membersType = new TypeToken<List<UUID>>() {}.getType();
            document.put("members", OxygenBukkit.PLAIN_GSON.toJson(squad.getMembers(), membersType));
            Type captainsType = new TypeToken<List<UUID>>() {}.getType();
            document.put("captains", OxygenBukkit.PLAIN_GSON.toJson(squad.getCaptains(), captainsType));
            if (squad.getPassword() != null) {
                document.append("password", squad.getPassword());
            }
            document.put("dateCreated", squad.getDateCreated());

            collection.insertOne(document);
        }
    }

    public Squad getSquad(String name) {
        return squads.stream().filter(squad -> squad.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void broadcastMessage(String displayName, String message, String server) {
        for (Squad squad : squads) {
            squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("settings.broadcast-format").replace("%server%", server).replace("%player%", displayName).replace("%message%", message)));
        }
    }
}
