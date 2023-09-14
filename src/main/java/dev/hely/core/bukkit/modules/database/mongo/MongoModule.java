package dev.hely.core.bukkit.modules.database.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import lombok.Getter;
import org.bson.Document;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name OxygenGlobal
 */
@Getter
public class MongoModule implements ModuleLoader {

    private boolean enabled;

    @Getter private MongoDatabase mongoDatabase;

    public MongoCollection<Document> profilesCollection;
    public MongoCollection<Document> ranksCollection;
    public MongoCollection<Document> punishmentsCollection;
    public MongoCollection<Document> tagsCollection;
    public MongoCollection<Document> requestsCollection;
    public MongoCollection<Document> playerLogsCollection;
    public MongoCollection<Document> storeCollection;
    public MongoCollection<Document> logsCollection;
    public MongoCollection<Document> couponsCollection;
    public MongoCollection<Document> filtersCollection;

    @Override
    public void enable() {
        if (OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getBoolean("database.mongo.auth.enabled")) {
            mongoDatabase = new MongoClient(
                    new ServerAddress(
                            OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.address"),
                            OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getInt("database.mongo.port")),
                    MongoCredential.createCredential(
                            OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.auth.user"),
                            "admin",
                            OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.auth.password").toCharArray()),
                    MongoClientOptions.builder().build()
            ).getDatabase(OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
        } else {
            mongoDatabase = new MongoClient(
                    OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.address"),
                    OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getInt("database.mongo.port")
            ).getDatabase(OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
        }

        profilesCollection = mongoDatabase.getCollection("profiles");
        ranksCollection = mongoDatabase.getCollection("ranks");
        punishmentsCollection = mongoDatabase.getCollection("punishments");
        tagsCollection = mongoDatabase.getCollection("tags");
        requestsCollection = mongoDatabase.getCollection("requests");
        playerLogsCollection = mongoDatabase.getCollection("playerLogs");
        storeCollection = mongoDatabase.getCollection("store");
        logsCollection = mongoDatabase.getCollection("playerLogs");
        couponsCollection = mongoDatabase.getCollection("storeCoupons");
        filtersCollection = mongoDatabase.getCollection("chatFilters");

        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getName() {
        return "Mongo Manager";
    }
}
