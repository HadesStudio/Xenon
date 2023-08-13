package dev.hely.core.bungee.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server
//   Date: Sunday, August 13, 2023 - 1:14 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bungee.OxygenBungee;
import dev.hely.lib.CC;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
@RequiredArgsConstructor
public class Server {
    private String server_name;
    private String server_address;
    private Integer server_port;

    private OxygenBungee plugin = OxygenBungee.getInstance();

    public void save() {
        Document document = new Document();
        document.put("serverName", this.server_name);
        document.put("serverAddress", this.server_address);
        document.put("serverPort", this.server_port);

        plugin.getMongoManager().getServers().replaceOne(Filters.eq("serverName", server_name), document, new UpdateOptions().upsert(true));
    }

    public void load() {
        Document document = plugin.getMongoManager().getServers().find(Filters.eq("serverName", server_name)).first();

        if (document != null) {
            setServer_name(document.getString("serverName"));
            setServer_address(document.getString("serverAddress"));
            setServer_port(document.getInteger("serverPort"));
        }
    }

    public void removeServer() {
        plugin.getMongoManager().getServers().deleteOne(Filters.eq("serverName", server_name));
    }
}
