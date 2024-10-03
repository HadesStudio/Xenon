package net.ascend.core.bukkit.modules.database.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import lombok.Getter;
import org.bson.Document;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Xenon
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
    public MongoCollection<Document> logsCollection;
    public MongoCollection<Document> filtersCollection;
    public MongoCollection<Document> notificationsCollection;

    @Override
    public void onEnable() {
        if (!XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getBoolean("database.mongo.connection.enabled")) {
            if (XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getBoolean("database.mongo.auth.enabled")) {
                ServerAddress serverAddress = new ServerAddress(
                        XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.address"),
                        XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getInt("database.mongo.port"));
                MongoCredential credential = MongoCredential.createCredential(XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.auth.user"),
                        XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"),
                        XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.auth.password").toCharArray());


                mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build()).getDatabase(XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));

            } else {
                mongoDatabase = new MongoClient(
                        XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.address"),
                        XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getInt("database.mongo.port")
                ).getDatabase(XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
            }
        } else {
            MongoClient client = new MongoClient(new MongoClientURI(XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.connection.url")));
            mongoDatabase = client.getDatabase(XenonBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));

        }

        profilesCollection = mongoDatabase.getCollection("profiles");
        ranksCollection = mongoDatabase.getCollection("ranks");
        punishmentsCollection = mongoDatabase.getCollection("punishments");
        tagsCollection = mongoDatabase.getCollection("tags");
        requestsCollection = mongoDatabase.getCollection("requests");
        logsCollection = mongoDatabase.getCollection("playerLogs");
        filtersCollection = mongoDatabase.getCollection("chatFilters");
        notificationsCollection = mongoDatabase.getCollection("notifications");

        enabled = true;
    }

    @Override
    public void onDisable() {
        enabled = false;
    }

    @Override
    public void onReload() {

    }
}
