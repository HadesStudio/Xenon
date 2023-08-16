package dev.hely.core.bukkit;

import com.github.lalyos.jfiglet.FigletFont;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import dev.hely.core.bukkit.chat.ChatModule;
import dev.hely.core.bukkit.client.ClientModule;
import dev.hely.core.bukkit.coins.StoreManager;
import dev.hely.core.bukkit.coins.sales.SaleManager;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.modules.database.mongo.MongoModule;
import dev.hely.core.bukkit.modules.database.redis.RedisModule;
import dev.hely.core.bukkit.modules.listener.ListenerModule;
import dev.hely.core.bukkit.profile.ProfileManager;
import dev.hely.core.bukkit.profile.packet.ProfilePacketListener;
import dev.hely.core.bukkit.profile.tfa.TFAManager;
import dev.hely.core.bukkit.punishment.packet.PunishmentPacketListener;
import dev.hely.core.bukkit.ranks.rank.RankManager;
import dev.hely.core.bukkit.server.BukkitServerListener;
import dev.hely.core.bukkit.server.BukkitServerManager;
import dev.hely.core.bukkit.server.queue.QueueManager;
import dev.hely.core.bukkit.server.queue.runnables.MainQueueRunnable;
import dev.hely.core.bukkit.server.queue.runnables.QueuePositionRunnable;
import dev.hely.core.bukkit.server.queue.runnables.QueueUpdateRunnable;
import dev.hely.core.bukkit.server.runnable.HeartBeatRunnable;
import dev.hely.core.bukkit.server.runnable.ServerRunnable;
import dev.hely.core.bukkit.staff.requests.RequestManager;
import dev.hely.core.bukkit.tag.TagManager;
import dev.hely.core.bukkit.tag.packet.TagPacketListener;
import dev.hely.core.bukkit.tasks.NameMCReminderTask;
import dev.hely.core.bukkit.utils.Config;
import dev.hely.core.bukkit.utils.command.CommandManager;
import dev.hely.core.bukkit.utils.command.SubCommandManager;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.server.data.server.data.StatusData;
import dev.hely.core.bungee.server.packets.ServerUpdatePacket;
import dev.hely.lib.CC;
import dev.hely.lib.Tasks;
import dev.hely.lib.adapter.BlockVectorAdapter;
import dev.hely.lib.adapter.LocationAdapter;
import dev.hely.lib.adapter.PotionEffectAdapter;
import dev.hely.lib.adapter.VectorAdapter;
import dev.hely.lib.menu.MenuManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name OxygenGlobal
 */

@Getter
public class OxygenBukkit extends JavaPlugin {
    @Getter private static OxygenBukkit instance;
    private long LAST_START_TIME;
    private List<Config> configList;
    private boolean stopping;

    private Tasks tasks;
    private Config licenseConfig;
    private Config databaseConfig;
    private Config configuration;
    private Config languageConfig;
    private Config ranksConfig;
    private Config rankSettingsConfig;
    private Config tagConfig;
    private Config profileMenuConfig;
    private Config historyMenuConfig;
    private Config requestsMenuConfig;
    private Config logsMenuConfig;
    private Config editProfileMenuConfig;
    private Config tagsMenuConfig;
    private Config serversMenuConfig;
    private Config coinShopConfig;
    private GoogleAuthenticator googleAuthenticator;
    private Config serverConfig;

    public static final Gson PLAIN_GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();

    private List<ModuleLoader> modules;

    private MongoModule mongoModule;
    private RedisModule redisModule;

    private ProfileManager profileManager;
    private RankManager rankManager;
    private TagManager tagManager;
    private BukkitServerManager serverManager;
    private CommandManager commandManager;
    private SubCommandManager subCommandManager;
    private ListenerModule listenerModule;
    private ClientModule clientManager;
    private ChatModule chatManager;
    private RequestManager requestHandler;
    private SaleManager saleManager;
    private StoreManager storeManager;
    private TFAManager tfaManager;
    private QueueManager queueManager;

    private boolean debug_mode;

    public void onEnable() {
        instance = this;
        LAST_START_TIME = System.currentTimeMillis();
        configList = new ArrayList<>();
        stopping = false;

        startupMessages();
        setupConfig();
        setDebugMode(true);
        initModules();
        setupModules();
        setupPackets();
        registerTasks();

        new QueuePositionRunnable().runTaskTimerAsynchronously(this,0L,3 * 20L);
        new QueueUpdateRunnable().runTaskTimerAsynchronously(this,0L,3 * 20L);
        new MainQueueRunnable().runTaskTimerAsynchronously(this,0L,3 * 20L);
        new ServerRunnable().runTaskTimerAsynchronously(this,0L,3 * 20L);
        new HeartBeatRunnable().runTaskTimerAsynchronously(this,0L,3 * 20L);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");
    }

    public void onDisable() {
        stopping = true;

        final Server server = new Server(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("group"), OxygenBukkit.getInstance(), false);

        server.setStatus(StatusData.OFFLINE);
        server.getOnlinePlayers().set(0);
        server.cache(OxygenBukkit.getInstance().getRedisModule().getJedisPool());
        redisModule.sendPacket(new ServerUpdatePacket(server.getName(), server.getPort(), server.toJsonObject()));

        modules.forEach(this::unloadModule);
    }

    public long getUptime() {
        return System.currentTimeMillis() - LAST_START_TIME;
    }

    public void startupMessages() {
        try {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&d&l" + FigletFont.convertOneLine("Oxygen Core")));
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&dAuthors: &c" + OxygenBukkit.getInstance().getDescription().getAuthors()));
            bukkitCommandSender.sendMessage(CC.translate("&dDiscord: &cdiscord.gg/helydev"));
            bukkitCommandSender.sendMessage(CC.translate("&dDebug Mode: " + displayDebugStatus()));
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
        } catch (IOException ignored) {
            OxygenBukkit.getInstance().getLogger().info("Their was an error while displaying the startup messages..");
            OxygenBukkit.getInstance().getLogger().info("Please open a discord ticket on our discord.");
            this.onDisable();
            Bukkit.shutdown();
        }
    }

    public void setupConfig(){
        try {
            this.licenseConfig = new Config(OxygenBukkit.getInstance(), "license");
            this.configList.add(licenseConfig);
            this.configuration = new Config(OxygenBukkit.getInstance(), "config");
            this.configList.add(configuration);
            this.languageConfig = new Config(OxygenBukkit.getInstance(), "language");
            this.configList.add(licenseConfig);
            this.databaseConfig = new Config(OxygenBukkit.getInstance(), "databases" + File.separator + "databases");
            this.configList.add(databaseConfig);
            this.ranksConfig = new Config(OxygenBukkit.getInstance(), "ranks" + File.separator + "ranks");
            this.configList.add(ranksConfig);
            this.rankSettingsConfig = new Config(OxygenBukkit.getInstance(), "ranks" + File.separator + "rank-settings");
            this.configList.add(rankSettingsConfig);
            this.tagConfig = new Config(OxygenBukkit.getInstance(), "tags");
            this.configList.add(tagConfig);
            this.serverConfig = new Config(OxygenBukkit.getInstance(), "server-data");
            this.configList.add(serverConfig);
            this.profileMenuConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "profile-menu");
            this.configList.add(profileMenuConfig);
            this.historyMenuConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "history-menu");
            this.configList.add(historyMenuConfig);
            this.requestsMenuConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "requests-menu");
            this.configList.add(requestsMenuConfig);
            this.logsMenuConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "log-menu");
            this.configList.add(logsMenuConfig);
            this.editProfileMenuConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "edit-profile-menu");
            this.configList.add(editProfileMenuConfig);
            this.tagsMenuConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "tags-menu");
            this.configList.add(tagsMenuConfig);
            this.serversMenuConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "servers-menu");
            this.configList.add(serversMenuConfig);
            this.coinShopConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "coin-shop");
            this.configList.add(coinShopConfig);

            Locale.load();
        } catch (IOException | InvalidConfigurationException e) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
            bukkitCommandSender.sendMessage(CC.translate("&6Oxygen Core - Configuration Error"));
            bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
            bukkitCommandSender.sendMessage(CC.translate("&cError:"));
            e.printStackTrace();
            bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
        }
    }

    public void registerTasks() {
        new NameMCReminderTask().runTaskTimerAsynchronously(OxygenBukkit.getInstance(), 20L * 60 * OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("name-mc.reminder-timer"), 20L * 60 * OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("name-mc.reminder-timer"));
    }

    public void initModules(){
        tasks = new Tasks(OxygenBukkit.getInstance());

        redisModule = new RedisModule();
        mongoModule = new MongoModule();

        googleAuthenticator = new GoogleAuthenticator();
        saleManager = new SaleManager();
        clientManager = new ClientModule();
        commandManager = new CommandManager();
        subCommandManager = new SubCommandManager();
        listenerModule = new ListenerModule();
        serverManager = new BukkitServerManager();
        rankManager = new RankManager();
        tagManager = new TagManager();
        chatManager = new ChatModule();
        requestHandler = new RequestManager();
        storeManager = new StoreManager();
        tfaManager = new TFAManager();
        queueManager = new QueueManager();
    }

    public void setupModules(){
        MenuManager.INSTANCE.onEnable(OxygenBukkit.getInstance());
        profileManager = new ProfileManager();
        modules = new ArrayList<>();

        this.loadModule(mongoModule);
        this.loadModule(redisModule);
        this.loadModule(chatManager);
        this.loadModule(clientManager);
        this.loadModule(commandManager);
        this.loadModule(subCommandManager);
        this.loadModule(listenerModule);
        this.loadModule(requestHandler);
        this.loadModule(rankManager);
        this.loadModule(tagManager);
        this.loadModule(serverManager);
        this.loadModule(storeManager);
        this.loadModule(tfaManager);
        this.loadModule(queueManager);
    }

    public void setupPackets(){
        redisModule.registerListener(new ProfilePacketListener());
        redisModule.registerListener(new PunishmentPacketListener(this));
        redisModule.registerListener(new TagPacketListener(tagManager));
        redisModule.registerListener(new BukkitServerListener());

        redisModule.subscribeJedis();
    }

    private void loadModule(ModuleLoader module) {
        if (!module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.enable();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&dModules: &aSuccessfully loaded &a&n"  + module.getName() + " Module&a."));
            }
            modules.add(module);
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
        this.setupConfig();
    }

    public void debug_message(String message) {
        if (this.debug_mode) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(String.format("&c[Oxygen Debug] &4%s", CC.translate(message))));
        }
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
