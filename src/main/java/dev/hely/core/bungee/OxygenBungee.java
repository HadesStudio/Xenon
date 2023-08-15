package dev.hely.core.bungee;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee
//   Date: Thursday, August 03, 2023 - 4:51 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.github.lalyos.jfiglet.FigletFont;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bungee.commands.HubCommand;
import dev.hely.core.bungee.commands.ServerManagerCommand;
import dev.hely.core.bungee.databases.mongo.MongoManager;
import dev.hely.core.bungee.listener.PacketListener;
import dev.hely.core.bungee.listener.PermissionListener;
import dev.hely.core.bungee.maintenance.MaintenanceManager;
import dev.hely.core.bungee.packet.PermissionPacketListener;
import dev.hely.core.bungee.databases.redis.RedisBungeeModule;
import dev.hely.core.bungee.server.ServerManager;
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.server.data.proxy.data.ProxyServerRegion;
import dev.hely.core.bungee.server.data.proxy.data.ProxyServerStatus;
import dev.hely.core.bungee.server.listeners.ServerListener;
import dev.hely.core.bungee.server.packets.ProxyUpdatePacket;
import dev.hely.core.bungee.server.runnable.BungeeRunnable;
import dev.hely.core.bungee.utils.BungeeCC;
import dev.hely.core.bungee.utils.BungeeConfig;
import dev.hely.lib.CC;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
public class OxygenBungee extends Plugin {
    @Getter private static OxygenBungee instance;
    private boolean debug_mode;

    //Configs:
    private BungeeConfig databaseConfig;
    private BungeeConfig bungeeConfig;
    private BungeeConfig languageConfig;
    private BungeeConfig serverConfig;

    private RedisBungeeModule redisBungeeModule;
    private List<ModuleLoader> modules;
    private MaintenanceManager maintenanceManager;
    private MongoManager mongoManager;
    private ServerManager serverManager;

    private final Map<UUID, List<String>> playerPermissions = new HashMap<>();
    public static final Gson PLAIN_GSON = (new GsonBuilder()).serializeNulls().create();

    @Override
    public void onEnable() {
        instance = this;

        this.startupMessages();
        this.registerConfigs();

        initModules();
        setupModules();
        setupPackets();
        setupCommands();
        setupListeners();
        setupRunnables();
    }

    public void registerConfigs() {
        bungeeConfig = new BungeeConfig(this, "bungee-config");
        databaseConfig = new BungeeConfig(this, "bungee-databases");
        languageConfig = new BungeeConfig(this, "bungee-language");
        serverConfig = new BungeeConfig(this, "server-data");

        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully loaded all configuration files."));
    }

    public void startupMessages() {
        try {
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6&l" + FigletFont.convertOneLine("Oxygen Core")));
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Authors: &c" + OxygenBungee.getInstance().getDescription().getAuthor()));
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Discord: &cdiscord.gg/helydev"));
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Debug Mode: " + displayDebugStatus()));
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
        } catch (IOException ignored) {
            OxygenBungee.getInstance().getLogger().info("Their was an error while displaying the startup messages..");
            OxygenBungee.getInstance().getLogger().info("Please open a discord ticket on our discord.");
            this.onDisable();
        }
    }

    @Override
    public void onDisable() {
        Proxy proxy = new Proxy(OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("server-name"), ProxyServerRegion.valueOf(OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("region")),this);

        proxy.setStatus(ProxyServerStatus.OFFLINE);
        proxy.getOnlinePlayers().set(0);

        this.redisBungeeModule.sendPacket(new ProxyUpdatePacket(proxy.getName(), proxy.getPort(), proxy.toJsonObject()));

        this.redisBungeeModule.disable();

        modules.forEach(this::unloadModule);
    }


    public void initModules(){
        redisBungeeModule = new RedisBungeeModule();
        maintenanceManager = new MaintenanceManager();
        mongoManager = new MongoManager();
        serverManager = new ServerManager();
    }

    public void setupModules(){
        modules = new ArrayList<>();

        this.loadModule(maintenanceManager);
        this.loadModule(mongoManager);
        this.loadModule(serverManager);

        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered &a&n" + modules.size() + " Modules&a."));
    }

    public void setupRunnables() {
        this.getProxy().getScheduler().schedule(this,new BungeeRunnable(),0,3L, TimeUnit.SECONDS);

        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered all runnables."));
    }

    public void setupPackets() {
        redisBungeeModule.registerListener(new PermissionPacketListener());

        redisBungeeModule.subscribeJedis();

        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered all packets."));
    }

    public void setupCommands() {
        this.getProxy().getPluginManager().registerCommand(this, new ServerManagerCommand());
        this.getProxy().getPluginManager().registerCommand(this, new HubCommand());

        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered all commands."));
    }

    public void setupListeners() {
        this.getProxy().getPluginManager().registerListener(this, new ServerListener());
        this.getProxy().getPluginManager().registerListener(this, new PermissionListener());
        this.getProxy().getPluginManager().registerListener(this, new PacketListener());

        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered all listeners."));
    }


    // Debug Mode -- Useful for errors if setup properly...
    public boolean isDebugMode() {
        return this.debug_mode;
    }

    public String displayDebugStatus() {
        if (this.debug_mode) {
            return "&aTrue";
        } else {
            return "&cFalse";
        }
    }

    public void setDebugMode(boolean status) {
        this.debug_mode = status;
        this.getBungeeConfig().getConfiguration().set("debug-mode", this.debug_mode);
        this.registerConfigs();
    }

    public void debug_message(String message) {
        if (this.debug_mode) {
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(String.format("&c[Oxygen-Bungee Debug] &4%s", CC.translate(message))));
        }
    }

    public void debug_message(String message, ModuleLoader module) {
        if (this.debug_mode) {
            if (module != null) {
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(String.format("&c[Oxygen-Bungee Debug (%s)] &4%s", module.getName(), CC.translate(message))));
            } else {
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(message));
            }
        }
    }

    private void loadModule(ModuleLoader module) {
        if (!module.isEnabled()) {
            module.enable();
            if (this.debug_mode) {
                debug_message(BungeeCC.translate("&6Modules: &aSuccessfully loaded &a&n"  + module.getName() + " Module&a."));
            }
            modules.add(module);
        }
    }

    private void unloadModule(ModuleLoader module) {
        if (module.isEnabled()) {
            module.disable();
            if (this.debug_mode) {
                debug_message(BungeeCC.translate("&6Modules: &aSuccessfully unloaded &a&n"  + module.getName() + " Module&c."));
            }
        }
    }

    public void reloadModule(ModuleLoader module) {
        if (module.isEnabled()) {
            module.reload();
            if (this.debug_mode) {
                debug_message(BungeeCC.translate("&6Modules: &aSuccessfully reloaded &a&n"  + module + " Module&a."));
            }
        }
    }

}
