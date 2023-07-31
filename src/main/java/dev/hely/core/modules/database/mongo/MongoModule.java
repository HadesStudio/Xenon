package dev.hely.core.modules.database.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.hely.core.Oxygen;
import dev.hely.core.modules.ModuleLoader;
import lombok.Getter;
import org.bson.Document;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
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

    @Override
    public void enable() {
        if (!Oxygen.getInstance().getDatabaseConfig().getConfiguration().getBoolean("database.mongo.connection.enabled")) {
            if (Oxygen.getInstance().getDatabaseConfig().getConfiguration().getBoolean("database.mongo.auth.enabled")) {
                ServerAddress serverAddress = new ServerAddress(Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.address"), Oxygen.getInstance().getDatabaseConfig().getConfiguration().getInt("database.mongo.port"));
                MongoCredential credential = MongoCredential.createCredential(Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.auth.user"),
                        Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"),
                        Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.auth.password").toCharArray());

                mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build()).getDatabase(Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
            } else {
                mongoDatabase = new MongoClient(Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.address"), Oxygen.getInstance().getDatabaseConfig().getConfiguration().getInt("database.mongo.port")).getDatabase(Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
            }
        }else{
            MongoClient client = new MongoClient(new MongoClientURI(Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.connection.url")));
            mongoDatabase = client.getDatabase(Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
        }

        profilesCollection = mongoDatabase.getCollection("profiles");
        ranksCollection = mongoDatabase.getCollection("ranks");
        punishmentsCollection = mongoDatabase.getCollection("punishments");
        tagsCollection = mongoDatabase.getCollection("tags");
        requestsCollection = mongoDatabase.getCollection("requests");

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
