package dev.hely.core.managers.database.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.modules.ModuleLoader;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
 */
@Getter
public class MongoManager implements ModuleLoader {
    public static final String MODULE_NAME = "Mongo Manager";
    private boolean enabled;

    public MongoCollection<Document> profilesCollection;
    public MongoCollection<Document> ranksCollection;
    public MongoCollection<Document> punishmentsCollection;
    public MongoCollection<Document> tagsCollection;

    @Getter private MongoDatabase mongoDatabase;

    @Override
    public void enable() {
        if (!Oxygen.getInstance().getDatabase_config().getConfiguration().getBoolean("database.mongo.connection.enabled")) {
            if (Oxygen.getInstance().getDatabase_config().getConfiguration().getBoolean("database.mongo.auth.enabled")) {
                ServerAddress serverAddress = new ServerAddress(Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.address"), Oxygen.getInstance().getDatabase_config().getConfiguration().getInt("database.mongo.port"));
                MongoCredential credential = MongoCredential.createCredential(Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.auth.user"),
                        Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.database_name"),
                        Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.auth.password").toCharArray());

                mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build()).getDatabase(Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.database_name"));
            } else {
                mongoDatabase = new MongoClient(Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.address"), Oxygen.getInstance().getDatabase_config().getConfiguration().getInt("database.mongo.port")).getDatabase(Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.database_name"));
            }
        }else{
            MongoClient client = new MongoClient(new MongoClientURI(Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.connection.url")));
            mongoDatabase = client.getDatabase(Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.mongo.database_name"));
        }

        profilesCollection = mongoDatabase.getCollection("profiles");
        ranksCollection = mongoDatabase.getCollection("ranks");
        punishmentsCollection = mongoDatabase.getCollection("punishments");
        tagsCollection = mongoDatabase.getCollection("tags");

        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
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
        return MODULE_NAME;
    }

    public boolean doesCollectionExist(String name) {
        return this.mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains(name);
    }
}
