package dev.hely.core;

import com.github.lalyos.jfiglet.FigletFont;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.hely.core.managers.database.mongo.MongoManager;
import dev.hely.core.managers.database.redis.RedisManager;
import dev.hely.core.managers.modules.ModuleLoader;
import dev.hely.core.managers.server.ServerManager;
import dev.hely.core.profile.ProfileManagement;
import dev.hely.core.profile.ProfileManager;
import dev.hely.core.ranks.manager.RankManager;
import dev.hely.lib.CC;
import dev.hely.lib.adapter.BlockVectorAdapter;
import dev.hely.lib.adapter.LocationAdapter;
import dev.hely.lib.adapter.PotionEffectAdapter;
import dev.hely.lib.adapter.VectorAdapter;
import dev.hely.lib.configuration.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
 */

@Getter
public class Oxygen extends JavaPlugin {
    private Map<String, ModuleLoader> modules;
    @Getter private static Oxygen instance;
    @Getter private Config database_config;
    @Getter private Config config;
    @Getter private Config ranksConfig;

    private MongoManager mongoManager;
    private ProfileManager profileManager;
    private RedisManager redisManager;
    private ServerManager serverManager;
    private RankManager rankManager;
    private ProfileManagement profileManagement;

    private boolean debug_mode;
    public static final Gson PLAIN_GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();

    @Override
    public void onEnable() {
        instance = this;
        this.modules = modules;

        startupMessages();
        setupConfig();
        initModules();
        setupModules();
    }

    @Override
    public void onDisable() {
        for (ModuleLoader module : this.modules.values()) {
            this.unloadModule(module);
            modules.remove(module);
        }
    }

    public void startupMessages() {
        try {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&d&l" + FigletFont.convertOneLine("Oxygen Core")));
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&dAuthors: &c" + getDescription().getAuthors()));
            bukkitCommandSender.sendMessage(CC.translate("&dDiscord: &cdiscord.gg/helydev"));
            bukkitCommandSender.sendMessage(CC.translate("&dDebug Mode: " + displayDebugStatus()));
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
        } catch (IOException ignored) {
            this.getLogger().info("Their was an error while displaying the startup messages..");
            this.getLogger().info("Please open a discord ticket on our discord.");
            this.onDisable();
            Bukkit.shutdown();
        }
    }

    public void setupConfig(){
        this.config = new Config(this, "config.yml");
        this.database_config = new Config(this, "databases" + File.separator + "databases.yml");
        this.ranksConfig = new Config(this, "ranks" + File.separator + "ranks.yml");

    }

    public void setupModules(){
        profileManager = new ProfileManager(); // Does not use Module System.
        profileManagement = new ProfileManagement(this); // Does not use Module System.

        this.loadModule(mongoManager);
        this.loadModule(redisManager);
        this.loadModule(serverManager);
        this.loadModule(rankManager);
    }
    public void initModules(){
        redisManager = new RedisManager();
        mongoManager = new MongoManager();
        serverManager = new ServerManager();
        rankManager = new RankManager();

    }



    // Module Manager -- The below is for the module system.
    private void loadModule(ModuleLoader module) {
        if (!module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.enable();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&dModules: &aSuccessfully loaded &a&n"  + module + " Module&a."));
            }
        }
    }
    private void unloadModule(ModuleLoader module) {
        if (module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.disable();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&dModules: &cSuccessfully unloaded &c&n"  + module + " Module&c."));
            }
        }
    }
    public void reloadModule(ModuleLoader module) {
        if (module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.reload();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&dModules: &aSuccessfully reloaded &a&n"  + module + " Module&a."));
            }
        }
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
        this.getConfig().getConfiguration().set("plugin-settings.debug-mode", this.debug_mode);
        this.getConfig().save();
    }
}
