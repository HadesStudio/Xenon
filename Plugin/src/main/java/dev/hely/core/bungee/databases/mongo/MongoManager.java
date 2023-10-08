package dev.hely.core.bungee.databases.mongo;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.databases.mongo
//   Date: Sunday, August 13, 2023 - 1:08 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bungee.OxygenBungee;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoManager implements ModuleLoader {

    private boolean enabled;

    @Getter
    private MongoDatabase mongoDatabase;

    public MongoCollection<Document> servers;

    @Override
    public void enable() {
        if (!OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getBoolean("database.mongo.connection.enabled")) {
            if (OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getBoolean("database.mongo.auth.enabled")) {
                ServerAddress serverAddress = new ServerAddress(OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.address"), OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getInt("database.mongo.port"));
                MongoCredential credential = MongoCredential.createCredential(OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.auth.user"),
                        OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"),
                        OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.auth.password").toCharArray());

                mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build()).getDatabase(OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
            } else {
                mongoDatabase = new MongoClient(OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.address"), OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getInt("database.mongo.port")).getDatabase(OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
            }
        } else {
            MongoClient client = new MongoClient(new MongoClientURI(OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.connection.url")));
            mongoDatabase = client.getDatabase(OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.mongo.database_name"));
        }

        servers = mongoDatabase.getCollection("servers");

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