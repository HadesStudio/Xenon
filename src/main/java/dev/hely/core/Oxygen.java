package dev.hely.core;

import com.github.lalyos.jfiglet.FigletFont;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.hely.core.ranks.rank.RankManager;
import dev.hely.core.managers.database.redis.RedisManager;
import dev.hely.core.managers.database.mongo.MongoManager;
import dev.hely.core.managers.modules.ModuleLoader;
import dev.hely.core.profile.ProfileManager;
import dev.hely.core.tag.TagManager;
import dev.hely.core.utils.command.CommandManager;
import dev.hely.lib.CC;
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

    @Getter private Config databaseConfig;
    @Getter private Config configuration;
    @Getter private Config ranksConfig;
    @Getter private Config rankSettingsConfig;

    private Map<String, ModuleLoader> modules;


    private MongoManager mongoManager;
    private RedisManager redisManager;

    private ProfileManager profileManager;
    private RankManager rankManager;
    private TagManager tagManager;
    private CommandManager commandManager;
    private MenuManager menuManager;

    private boolean debug_mode;
    public static final Gson PLAIN_GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();

    @Override
    public void onEnable() {
        instance = this;

        startupMessages();
        setupConfig();
        setDebugMode(true);
        initModules();
        setupModules();
    }

    @Override
    public void onDisable() {
        Iterator var1 = this.modules.values().iterator();

        while(var1.hasNext()) {
            this.unloadModule((ModuleLoader)var1.next());
            var1.remove();
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
        this.configuration = new Config(this, "config");
        this.databaseConfig = new Config(this, "databases" + File.separator + "databases");
        this.ranksConfig = new Config(this, "ranks" + File.separator + "ranks");
        this.rankSettingsConfig = new Config(this, "ranks" + File.separator + "rank-settings");

    }

    public void setupModules(){
        menuManager = MenuManager.INSTANCE;
        Oxygen.getInstance().getServer().getPluginManager().registerEvents(new MenuListener(this), this);

        profileManager = new ProfileManager(); // Does not use Module System.
        this.loadModule(commandManager);

        this.loadModule(mongoManager);
        this.loadModule(redisManager);
        this.loadModule(rankManager);
        this.loadModule(tagManager);
    }
    public void initModules(){
        commandManager = new CommandManager();

        redisManager = new RedisManager();
        mongoManager = new MongoManager();
        rankManager = new RankManager();
        tagManager = new TagManager();
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
        this.getConfiguration().getConfiguration().set("plugin-settings.debug-mode", this.debug_mode);
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
