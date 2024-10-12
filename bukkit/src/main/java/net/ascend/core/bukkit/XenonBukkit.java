package net.ascend.core.bukkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import es.hulk.licensesystem.LicenseImplementation;
import lombok.Getter;
import net.ascend.core.bukkit.chat.ChatManager;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.filter.FilterManager;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.modules.database.mongo.MongoModule;
import net.ascend.core.bukkit.modules.database.redis.RedisModule;
import net.ascend.core.bukkit.modules.listener.ListenerModule;
import net.ascend.core.bukkit.hook.placeholder.PlaceHolderAPIExpansion;
import net.ascend.core.bukkit.hook.placeholder.PlaceholderHook;
import net.ascend.core.bukkit.profile.ProfileManager;
import net.ascend.core.bukkit.profile.log.LogManager;
import net.ascend.core.bukkit.profile.notifications.NotificationManager;
import net.ascend.core.bukkit.profile.notifications.packet.NotificationPacketListener;
import net.ascend.core.bukkit.profile.packet.ProfilePacketListener;
import net.ascend.core.bukkit.punishment.packet.PunishmentPacketListener;
import net.ascend.core.bukkit.rank.grant.packet.GrantPacketListener;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.rank.rank.packet.RankPacketListener;
import net.ascend.core.bukkit.server.packet.ServerPacketListener;
import net.ascend.core.bukkit.server.ServerManager;
import net.ascend.core.bukkit.server.reboot.RebootManager;
import net.ascend.core.bukkit.staff.request.RequestManager;
import net.ascend.core.bukkit.staff.vanish.VanishManager;
import net.ascend.core.bukkit.tag.TagManager;
import net.ascend.core.bukkit.tag.packet.TagPacketListener;
import net.ascend.core.bukkit.util.command.CommandManager;
import net.ascend.core.bukkit.util.command.SubCommandManager;
import net.ascend.core.bukkit.util.prompt.PromptManager;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.TaskUtil;
import net.ascend.core.bukkit.lib.adapter.BlockVectorAdapter;
import net.ascend.core.bukkit.lib.adapter.LocationAdapter;
import net.ascend.core.bukkit.lib.adapter.VectorAdapter;
import net.ascend.core.bukkit.lib.configuration.Config;
import net.ascend.core.bukkit.lib.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Xenon
 */

@Getter
public class XenonBukkit extends JavaPlugin {

    @Getter
    private static XenonBukkit instance;
    private boolean fullyEnabled;

    private long LAST_START_TIME;

    private List<Config> configList;
    private Config licenseConfig;
    private Config databaseConfig;
    private Config configuration;
    private Config languageConfig;
    private Config ranksConfig;
    private Config rankSettingsConfig;
    private Config tagConfig;
    private Config menuConfig;



    private Config requestsMenuConfig;
    private Config tagsMenuConfig;

    private TaskUtil taskUtil;

    private List<ModuleLoader> modules;
    private MongoModule mongoModule;
    private RedisModule redisModule;

    private ProfileManager profileManager;
    private RankManager rankManager;
    private TagManager tagManager;
    private ServerManager serverManager;
    private CommandManager commandManager;
    private SubCommandManager subCommandManager;
    private ListenerModule listenerModule;
    private RequestManager requestHandler;
    private LogManager logManager;
    private RebootManager rebootManager;
    private FilterManager filterManager;
    private ChatManager chatManager;
    private NotificationManager notificationManager;
    private PromptManager promptManager;

    private VanishManager vanishManager;

    public static final Gson PLAIN_GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();

    public void onEnable() {
        this.instance = this;

        try {
            this.licenseConfig = new Config(this, "license");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        final String stringLicense = this.licenseConfig.getConfiguration().getString("license");
        LicenseImplementation license = new LicenseImplementation(
            false, stringLicense, getDescription().getName(), getDescription().getVersion()
        );

        if (!license.isLicenseValid()) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getScheduler().cancelTasks(this);
            return;
        }

        setupConfig();
        initModules();
        setupModules();
        setupPackets();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHook();
            new PlaceHolderAPIExpansion(this).register();
        }

        this.LAST_START_TIME = System.currentTimeMillis();
        this.fullyEnabled = true;
    }

    public void onDisable() {
        if (!this.fullyEnabled) return;

        PlaceholderHook.setENABLED(false);

        try {
            for (ModuleLoader module : modules) {
                if (module != null) {
                    module.onDisable();
                }
            }

            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoClassDefFoundError | NullPointerException e) {
            return;
        }
    }

    public void setupConfig() {
        this.configList = new ArrayList<>();

        try {
            this.configuration = new Config(XenonBukkit.getInstance(), "config");
            this.configList.add(configuration);
            this.languageConfig = new Config(XenonBukkit.getInstance(), "language");
            this.configList.add(languageConfig);
            this.databaseConfig = new Config(XenonBukkit.getInstance(), "databases" + File.separator + "databases");
            this.configList.add(databaseConfig);
            this.ranksConfig = new Config(XenonBukkit.getInstance(), "ranks" + File.separator + "ranks");
            this.configList.add(ranksConfig);
            this.rankSettingsConfig = new Config(XenonBukkit.getInstance(), "ranks" + File.separator + "rank-settings");
            this.configList.add(rankSettingsConfig);
            this.tagConfig = new Config(XenonBukkit.getInstance(), "tags");
            this.configList.add(tagConfig);
            this.requestsMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "requests-menu");
            this.configList.add(requestsMenuConfig);
            this.menuConfig = new Config(XenonBukkit.getInstance(), "menus");
            this.configList.add(menuConfig);
            this.tagsMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "tags-menu");
            this.configList.add(tagsMenuConfig);


            Locale.load();
        } catch (IOException | InvalidConfigurationException e) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&cXenon - Configuration Error"));
            bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
            bukkitCommandSender.sendMessage(CC.translate("&cError:"));
            e.printStackTrace();
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
        }
    }

    public void initModules() {
        this.taskUtil = new TaskUtil(XenonBukkit.getInstance());

        this.redisModule = new RedisModule();
        this.mongoModule = new MongoModule();

        this.commandManager = new CommandManager();
        this.subCommandManager = new SubCommandManager();
        this.listenerModule = new ListenerModule();


        this.rankManager = new RankManager();
        this.tagManager = new TagManager();
        this.serverManager = new ServerManager();
        this.rebootManager = new RebootManager();

        this.chatManager = new ChatManager();
        this.filterManager = new FilterManager();

        this.logManager = new LogManager();
        this.notificationManager = new NotificationManager();

        this.vanishManager = new VanishManager();
        this.requestHandler = new RequestManager();


        this.promptManager = new PromptManager(this);
    }

    public void setupModules() {
        MenuManager.INSTANCE.onEnable(XenonBukkit.getInstance());
        this.profileManager = new ProfileManager();

        this.modules = new ArrayList<>();

        this.loadModule(mongoModule);
        this.loadModule(redisModule);
        this.loadModule(chatManager);

        this.loadModule(commandManager);
        this.loadModule(subCommandManager);
        this.loadModule(listenerModule);

        this.loadModule(requestHandler);
        this.loadModule(rankManager);
        this.loadModule(tagManager);
        this.loadModule(notificationManager);
        this.loadModule(filterManager);
        this.loadModule(serverManager);
        this.loadModule(logManager);

    }

    public void setupPackets() {
        redisModule.registerListener(new ProfilePacketListener());
        redisModule.registerListener(new RankPacketListener());
        redisModule.registerListener(new PunishmentPacketListener(this));
        redisModule.registerListener(new TagPacketListener(tagManager));
        redisModule.registerListener(new GrantPacketListener());
        redisModule.registerListener(new ServerPacketListener(serverManager));
        redisModule.registerListener(new NotificationPacketListener());

        redisModule.subscribeJedis();
    }

    private void loadModule(ModuleLoader module) {
        module.onEnable();
        modules.add(module);
    }

    public void reloadModule(ModuleLoader module) {
        module.onReload();
    }

    public void sendMessage(String alert) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(alert));
    }

    public long getUptime() {
        return System.currentTimeMillis() - LAST_START_TIME;
    }
}
