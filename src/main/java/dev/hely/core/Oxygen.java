package dev.hely.core;

import com.github.lalyos.jfiglet.FigletFont;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.hely.core.configuration.Language;
import dev.hely.core.modules.listener.ListenerModule;
import dev.hely.core.profile.packet.ProfilePacketListener;
import dev.hely.core.ranks.rank.RankManager;
import dev.hely.core.modules.database.redis.RedisModule;
import dev.hely.core.modules.database.mongo.MongoModule;
import dev.hely.core.modules.ModuleLoader;
import dev.hely.core.profile.ProfileManager;
import dev.hely.core.server.ServerManager;
import dev.hely.core.tag.TagManager;
import dev.hely.core.tag.packet.TagPacketListener;
import dev.hely.core.utils.command.CommandManager;
import dev.hely.lib.CC;
import dev.hely.lib.Tasks;
import dev.hely.lib.adapter.BlockVectorAdapter;
import dev.hely.lib.adapter.LocationAdapter;
import dev.hely.lib.adapter.PotionEffectAdapter;
import dev.hely.lib.adapter.VectorAdapter;
import dev.hely.lib.configuration.Config;
import dev.hely.lib.menu.MenuListener;
import dev.hely.lib.menu.MenuManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
 */

@Getter
public class Oxygen extends JavaPlugin {

    @Getter private static Oxygen instance;

    private Tasks tasks;
    private Config databaseConfig;
    private Config configuration;
    private Config languageConfig;
    private Config ranksConfig;
    private Config rankSettingsConfig;
    private Config tagConfig;
    private Config profileMenuConfig;

    public static final Gson PLAIN_GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();

    private Map<String, ModuleLoader> modules;

    private MongoModule mongoModule;
    private RedisModule redisModule;

    private ProfileManager profileManager;
    private RankManager rankManager;
    private TagManager tagManager;
    private ServerManager serverManager;
    private CommandManager commandManager;
    private ListenerModule listenerModule;

    private boolean debug_mode;

    @Override
    public void onEnable() {
        instance = this;

        startupMessages();
        setupConfig();
        setDebugMode(true);
        initModules();
        setupModules();
        setupPackets();
    }

    @Override
    public void onDisable() {
        modules.forEach((s, moduleLoader) -> unloadModule(moduleLoader));
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
        this.configuration = new Config(this, "config");
        this.languageConfig = new Config(this, "language");
        this.databaseConfig = new Config(this, "databases" + File.separator + "databases");
        this.ranksConfig = new Config(this, "ranks" + File.separator + "ranks");
        this.rankSettingsConfig = new Config(this, "ranks" + File.separator + "rank-settings");
        this.tagConfig = new Config(this, "tags");
        this.profileMenuConfig = new Config(this, "menus" + File.separator + "profile-menu");

        Language.load();
    }
    public void initModules(){
        tasks = new Tasks(this);

        commandManager = new CommandManager();
        listenerModule = new ListenerModule();

        redisModule = new RedisModule();
        mongoModule = new MongoModule();
        rankManager = new RankManager();
        tagManager = new TagManager();
        serverManager = new ServerManager();
    }

    public void setupModules(){
        MenuManager.INSTANCE.onEnable(this);

        profileManager = new ProfileManager();

        modules = new HashMap<>();

        this.loadModule(commandManager);
        this.loadModule(listenerModule);

        this.loadModule(mongoModule);
        this.loadModule(redisModule);
        this.loadModule(rankManager);
        this.loadModule(tagManager);
        this.loadModule(serverManager);
    }

    public void setupPackets(){
        redisModule.registerListener(new ProfilePacketListener());
        redisModule.registerListener(new TagPacketListener(tagManager));

        redisModule.subscribeJedis();
    }

    // Module Manager -- The below is for the module system.
    private void loadModule(ModuleLoader module) {
        if (!module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.enable();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&dModules: &aSuccessfully loaded &a&n"  + module.getName() + " Module&a."));
            }
            modules.put(module.getName(), module);
        }
    }

    private void unloadModule(ModuleLoader module) {
        if (module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.disable();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&dModules: &cSuccessfully unloaded &c&n"  + module.getName() + " Module&c."));
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
        this.getConfiguration().getConfiguration().set("debug-mode", this.debug_mode);
        this.getConfiguration().save();
    }

    public void debug_message(String message, ModuleLoader module) {
        if (this.debug_mode) {
            if (module != null) {
                Bukkit.getConsoleSender().sendMessage(CC.translate(String.format("&c[Oxygen Debug (%s)] &4%s", module.getName(), CC.translate(message))));
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate(message));
            }
        }
    }
}
