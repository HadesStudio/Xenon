package dev.hely.core.bungee.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server
//   Date: Sunday, August 13, 2023 - 1:05 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.client.model.Filters;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.databases.redis.RedisBungeeModule;
import dev.hely.core.bungee.server.data.ServerData;
import dev.hely.core.bungee.server.events.ServerCreateEvent;
import dev.hely.core.bungee.server.events.ServerDeleteEvent;
import dev.hely.core.bungee.utils.BungeeCC;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.bson.Document;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerManager {
    @Getter private List<Server> servers = new ArrayList<>();
    private final RedisBungeeModule redisManager;

    @Getter
    private final HashMap<String, ServerData> serverDataHashMap = new HashMap<>();

    public ServerManager(RedisBungeeModule redisManager) {
        this.redisManager = redisManager;
    }

    public void createServer(CommandSender commandSender, String serverName, String serverAddress, int serverPort) {
        if (ProxyServer.getInstance().getServerInfo(serverName) != null) {
            commandSender.sendMessage(BungeeCC.translate("&calready exists idiota"));
            return;
        }

        redisManager.writeServerCreate(serverName, serverAddress, serverPort);
        ServerData serverData = serverDataHashMap.get(serverName);

        Server server = new Server();
        server.setServer_name(serverName);
        server.setServer_address(serverAddress);
        server.setServer_port(serverPort);
        server.save();
        ProxyServer.getInstance().getPluginManager().callEvent(new ServerCreateEvent(serverData));

        commandSender.sendMessage(BungeeCC.translate("&ccreating serverrrss - " + serverName));
    }

    public void deleteServer(CommandSender commandSender, String serverName) {
        if (ProxyServer.getInstance().getServerInfo(serverName) == null) {
            commandSender.sendMessage(BungeeCC.translate("&cThe server by this name does not exist."));
            return;
        }

        if (serverDataHashMap.containsKey(serverName)) {
            ServerData serverData = serverDataHashMap.get(serverName);
            redisManager.writeServerDelete(serverData);
            OxygenBungee.getInstance().getMongoManager().getServers().findOneAndDelete(Filters.eq("serverName", serverData.getServerName()));
            commandSender.sendMessage(BungeeCC.translate("&cdeleteing server - " + serverName));
            ProxyServer.getInstance().getPluginManager().callEvent(new ServerDeleteEvent(serverName));
        } else {
            OxygenBungee.getInstance().debug_message("&cThe server named '&6" + serverName + "&c' could not be found in the HashMap?");
        }


        commandSender.sendMessage(BungeeCC.translate("&cRequesting the server manager to delete &6" + serverName + " Server&c."));
    }

    public void listGameInstances(CommandSender commandSender) {
        commandSender.sendMessage(BungeeCC.translate("&cOnline servers on this bungee server puta;"));
        for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
            commandSender.sendMessage(BungeeCC.translate(" &c- &6" + info.getName()));
        }
    }

    public ServerInfo getRandomHub(List<String> excludeServers) {
        List<ServerInfo> hubServers = new ArrayList<>();

        for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
            if (serverInfo.getName().toLowerCase().contains(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("hub-name"))) {
                if (!excludeServers.contains(serverInfo.getName().toLowerCase()))
                    hubServers.add(serverInfo);
            }
        }

        ServerInfo hubServer;
        if (hubServers.size() == 0) {
            hubServer = ProxyServer.getInstance().getServers().values().iterator().next();
        } else {
            hubServer = hubServers.get(0);
        }

        for (ServerInfo serverInfo : hubServers) {
            if (hubServer.getPlayers().size() > serverInfo.getPlayers().size()) {
                hubServer = serverInfo;
            }
        }

        return hubServer;
    }

    public ServerInfo getRandomHub() {
        List<ServerInfo> hubServers = new ArrayList<>();

        for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
            if (serverInfo.getName().toLowerCase().contains(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("hub-name"))) {
                hubServers.add(serverInfo);
            }
        }

        ServerInfo hubServer;
        if (hubServers.size() == 0) {
            hubServer = ProxyServer.getInstance().getServers().values().iterator().next();
        } else {
            hubServer = hubServers.get(0);
        }

        for (ServerInfo serverInfo : hubServers) {
            if (hubServer.getPlayers().size() > serverInfo.getPlayers().size()) {
                hubServer = serverInfo;
            }
        }

        return hubServer;
    }


    public void loadServer(ServerData serverData) {
        serverDataHashMap.put(serverData.getServerName(), serverData);
        if (ProxyServer.getInstance().getServerInfo(serverData.getServerName()) == null) {
            if (getServer(serverData.getServerName()) == null) {
                Server server = new Server();
                server.setServer_name(serverData.getServerName());
                server.setServer_port(serverData.getInetSocketAddress().getPort());
                server.setServer_address(serverData.getInetSocketAddress().getAddress().getHostAddress());
                server.save();
                System.out.println("§cServer has been added to Mongo Database. [" + serverData.getServerName() + "]");
            } else {
                System.out.println("§cServer already exists in Mongo Database. [" + serverData.getServerName() + "]");
            }
            ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(serverData.getServerName(), serverData.getInetSocketAddress(), "", false);
            ProxyServer.getInstance().getServers().put(serverData.getServerName(), serverInfo);
            System.out.println("§cServer has been added. [" + serverData.getServerName() + "]");
            ProxyServer.getInstance().getPluginManager().callEvent(new ServerCreateEvent(serverData));
        } else {
            System.out.println("§cServer already exists. [" + serverData.getServerName() + "]");
        }

    }

    public Server getServer(String name) {
        return this.servers.stream().filter(server -> server.getServer_name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void removeLoadedServer(String serverName) {
        ProxyServer.getInstance().getServers().remove(serverName);
        serverDataHashMap.remove(serverName);
    }

    public void loadAllServers() {
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            if (OxygenBungee.getInstance().getServerManager().getServer(server.getName()) == null) {
                Server serverMongo = new Server();
                serverMongo.setServer_name(server.getName());
                serverMongo.setServer_port(server.getAddress().getPort());
                serverMongo.setServer_address(server.getAddress().getAddress().getHostAddress());
                System.out.print("Added " + server.getName() + " to mongo database");
            }
        }
        for (Document serverDocument : OxygenBungee.getInstance().getMongoManager().getServers().find()) {
            ServerData serverData1 = new ServerData(serverDocument.getString("serverName"), new InetSocketAddress(serverDocument.getString("serverAddress"), serverDocument.getInteger("serverPort")));
            OxygenBungee.getInstance().getServerManager().loadServer(serverData1);
            System.out.print("Loading server - " + serverData1.getServerName());
        }
    }

}