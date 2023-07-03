package dev.hely.core.database.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.hely.core.Oxygen;
import lombok.Getter;
import org.bson.Document;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
 */
@Getter
public class MongoManager {

    public MongoCollection<Document> profilesCollection;
    public MongoCollection<Document> ranksCollection;
    public MongoCollection<Document> punishmentsCollection;
    public MongoCollection<Document> tagsCollection;

    public MongoManager(Oxygen plugin){
        final MongoDatabase mongoDatabase;

        if (!plugin.getDatabase().getConfiguration().getBoolean("database.mongo.connection.enabled")) {

            if (plugin.getDatabase().getConfiguration().getBoolean("database.mongo.auth.enabled")) {
                ServerAddress serverAddress = new ServerAddress(plugin.getDatabase().getConfiguration().getString("database.mongo.address"), plugin.getDatabase().getConfiguration().getInt("database.mongo.port"));
                MongoCredential credential = MongoCredential.createCredential(plugin.getDatabase().getConfiguration().getString("database.mongo.auth.user"),
                        plugin.getDatabase().getConfiguration().getString("database.mongo.database_name"),
                        plugin.getDatabase().getConfiguration().getString("database.mongo.auth.password").toCharArray());

                mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build()).getDatabase(plugin.getDatabase().getConfiguration().getString("database.mongo.database_name"));
            } else {
                mongoDatabase = new MongoClient(plugin.getDatabase().getConfiguration().getString("database.mongo.address"), plugin.getDatabase().getConfiguration().getInt("database.mongo.port")).getDatabase(plugin.getDatabase().getConfiguration().getString("database.mongo.database_name"));
            }
        }else{
            MongoClient client = new MongoClient(new MongoClientURI(plugin.getDatabase().getConfiguration().getString("database.mongo.connection.url")));
            mongoDatabase = client.getDatabase(plugin.getDatabase().getConfiguration().getString("database.mongo.database_name"));
        }

        profilesCollection = mongoDatabase.getCollection("profiles");
        ranksCollection = mongoDatabase.getCollection("ranks");
        punishmentsCollection = mongoDatabase.getCollection("punishments");
        tagsCollection = mongoDatabase.getCollection("tags");
    }
}
