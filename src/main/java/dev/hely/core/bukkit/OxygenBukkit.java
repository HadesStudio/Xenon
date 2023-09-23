package dev.hely.core.bukkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import dev.hely.core.bukkit.announcements.AnnouncementManager;
import dev.hely.core.bukkit.chat.ChatModule;
import dev.hely.core.bukkit.client.ClientModule;
import dev.hely.core.bukkit.coins.StoreManager;
import dev.hely.core.bukkit.coins.coupons.CouponManager;
import dev.hely.core.bukkit.coins.sales.SaleManager;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.filter.FilterManager;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.modules.database.mongo.MongoModule;
import dev.hely.core.bukkit.modules.database.redis.RedisModule;
import dev.hely.core.bukkit.modules.listener.ListenerModule;
import dev.hely.core.bukkit.placeholder.PlaceHolderAPIExpansion;
import dev.hely.core.bukkit.placeholder.PlaceholderHook;
import dev.hely.core.bukkit.profile.ProfileManager;
import dev.hely.core.bukkit.profile.log.LogManager;
import dev.hely.core.bukkit.profile.packet.ProfilePacketListener;
import dev.hely.core.bukkit.profile.tfa.TFAManager;
import dev.hely.core.bukkit.punishment.packet.PunishmentPacketListener;
import dev.hely.core.bukkit.ranks.rank.RankManager;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.server.ServerListener;
import dev.hely.core.bukkit.server.ServerManager;
import dev.hely.core.bukkit.server.ServerState;
import dev.hely.core.bukkit.server.packets.ServerStateChangePacket;
import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.core.bukkit.server.queue.QueueManager;
import dev.hely.core.bukkit.server.queue.packets.QueuePacketListener;
import dev.hely.core.bukkit.server.reboot.RebootManager;
import dev.hely.core.bukkit.staff.requests.RequestManager;
import dev.hely.core.bukkit.staff.staffmode.StaffManager;
import dev.hely.core.bukkit.tag.TagManager;
import dev.hely.core.bukkit.tag.packet.TagPacketListener;
import dev.hely.core.bukkit.tasks.NameMCReminderTask;
import dev.hely.core.bukkit.vip.VIPRewardProvider;
import dev.hely.lib.configuration.Config;
import dev.hely.core.bukkit.utils.command.CommandManager;
import dev.hely.core.bukkit.utils.command.SubCommandManager;
import dev.hely.lib.util.CC;
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
    private Config serverConfig;
    private Config announcementConfig;
    private Config staffHistoryConfig;
    private Config vipMenuConfig;

    private GoogleAuthenticator googleAuthenticator;

    public static final Gson PLAIN_GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();

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
    private ClientModule clientManager;
    private RequestManager requestHandler;
    private SaleManager saleManager;
    private StoreManager storeManager;
    private TFAManager tfaManager;
    private QueueManager queueManager;
    private Queue queue;
    private LogManager logManager;
    private StaffManager staffManager;
    private CouponManager couponManager;
    private AnnouncementManager announcementManager;
    private RebootManager rebootManager;
    private Server serverData;
    private FilterManager filterManager;
    private ChatModule chatManager;
    private VIPRewardProvider vipRewardProvider;

    private boolean debug_mode;

    public void onEnable() {
        instance = this;
        LAST_START_TIME = System.currentTimeMillis();
        configList = new ArrayList<>();
        stopping = false;

        try {
            this.licenseConfig = new Config(this, "license");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        boolean verify = new SkyLicense("http://207.244.252.245:5555/api/license", this.licenseConfig.getConfiguration().getString("license")).verify();
        if (!verify) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getScheduler().cancelTasks(this);
            return;
        }

        setupConfig();
        setDebugMode(true);
        initModules();
        setupModules();
        setupPackets();
        registerTasks();

        logManager.registerAllLogs();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");

        this.serverData = new Server(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
        this.serverData.setStartTime(System.currentTimeMillis());
        serverData.load();
        serverData.save();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHook();
            new PlaceHolderAPIExpansion(this).register();
        }

        this.getRedisModule().sendPacket(new ServerStateChangePacket(serverData.getName(), ServerState.ONLINE));
    }

    public void onDisable() {
        PlaceholderHook.setENABLED(false);

        try {
            stopping = true;
            this.getServerData().save();
            this.getRedisModule().sendPacket(new ServerStateChangePacket(serverData.getName(), ServerState.OFFLINE));
            for (ModuleLoader module : modules) {
                if (module != null) {
                    module.disable();
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

    public long getUptime() {
        return System.currentTimeMillis() - LAST_START_TIME;
    }

    public void setupConfig() {
        try {
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
            this.staffHistoryConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "staff-history-menu");
            this.configList.add(staffHistoryConfig);
            this.announcementConfig = new Config(OxygenBukkit.getInstance(), "announcements");
            this.configList.add(announcementConfig);
            this.vipMenuConfig = new Config(OxygenBukkit.getInstance(), "menus" + File.separator + "vip-menu");
            this.configList.add(vipMenuConfig);
            Locale.load();
        } catch (IOException | InvalidConfigurationException e) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&6Oxygen Core - Configuration Error"));
            bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
            bukkitCommandSender.sendMessage(CC.translate("&cError:"));
            e.printStackTrace();
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
        }
    }

    public void registerTasks() {
        new NameMCReminderTask().runTaskTimerAsynchronously(OxygenBukkit.getInstance(), 20L * 60 * OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("name-mc.reminder-timer"), 20L * 60 * OxygenBukkit.getInstance().getConfiguration().getConfiguration().getInt("name-mc.reminder-timer"));
    }

    public void initModules() {
        this.queue = new Queue(this);
        tasks = new Tasks(OxygenBukkit.getInstance());

        redisModule = new RedisModule();
        mongoModule = new MongoModule();

        googleAuthenticator = new GoogleAuthenticator();
        saleManager = new SaleManager();
        clientManager = new ClientModule();
        commandManager = new CommandManager();
        subCommandManager = new SubCommandManager();
        listenerModule = new ListenerModule();
        rankManager = new RankManager();
        tagManager = new TagManager();
        chatManager = new ChatModule();
        requestHandler = new RequestManager();
        storeManager = new StoreManager();
        tfaManager = new TFAManager();
        logManager = new LogManager();
        staffManager = new StaffManager();
        announcementManager = new AnnouncementManager();

        queueManager = new QueueManager();
        serverManager = new ServerManager();
        couponManager = new CouponManager();
        rebootManager = new RebootManager();
        filterManager = new FilterManager();
    }

    public void setupModules() {
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
        this.loadModule(storeManager);
        this.loadModule(tfaManager);
        this.loadModule(staffManager);
        this.loadModule(filterManager);
        this.loadModule(serverManager);
        this.loadModule(couponManager);
        this.loadModule(announcementManager);
    }

    public void setupPackets() {
        redisModule.registerListener(new ProfilePacketListener());
        redisModule.registerListener(new PunishmentPacketListener(this));
        redisModule.registerListener(new TagPacketListener(tagManager));

        redisModule.registerListener(new ServerListener());
        redisModule.registerListener(new QueuePacketListener());

        redisModule.subscribeJedis();
    }

    private void loadModule(ModuleLoader module) {
        if (!module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.enable();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&aSuccessfully loaded &a&n"  + module.getName() + " Module&a."));
            }
            modules.add(module);
        }
    }

    private void unloadModule(ModuleLoader module) {
        if (module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.disable();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&cSuccessfully unloaded &c&n"  + module.getName() + " Module&c."));
            }
        }
    }

    public void reloadModule(ModuleLoader module) {
        if (module.isEnabled()) {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();
            module.reload();
            if (this.debug_mode) {
                bukkitCommandSender.sendMessage(CC.translate("&aSuccessfully reloaded &a&n"  + module + " Module&a."));
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


















    public static class SkyLicense {
        private final String apiUrl;
        private final String apiKey;
        private final String license;
        private final String product;
        private final String version;

        public SkyLicense(String apiUrl, String license) {
            this.apiKey = "7jn2zWa@{(RyCHtxq}g^CTnS/yUxuR}uAa]";
            this.apiUrl = apiUrl;
            this.license = license;
            this.product = instance.getDescription().getName();
            this.version = instance.getDescription().getVersion();
        }

        public boolean verify() {
            ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&6Attempting to verify your license.."));
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));


            String javaVersion = System.getProperty("java.version");
            String hwid = getHWID();
            String os = System.getProperty("os.name");

            String postData = createJSONData(javaVersion, hwid, os);

            try {
                HttpURLConnection connection = createConnection();
                sendData(connection, postData);
                String[] res = handleResponse(connection);
                if (res.length >= 2 && (res[0] != null && res[1] != null)) {

                    if (res[1].equals("000")) {
                        bukkitCommandSender.sendMessage(CC.translate("&6Successfully verified your license key.."));
                        bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
                        bukkitCommandSender.sendMessage(CC.translate("&6Loading plugin.."));
                        bukkitCommandSender.sendMessage(CC.translate(""));
                        bukkitCommandSender.sendMessage(CC.translate("&6&lOxygen Rank Core"));
                        bukkitCommandSender.sendMessage(CC.translate(""));
                        if (!version.equals(res[2])) {
                            bukkitCommandSender.sendMessage(CC.translate("&cThis plugin is outdated!"));
                            bukkitCommandSender.sendMessage(CC.translate("&cPlease download the new updated version in our discord."));
                            bukkitCommandSender.sendMessage(CC.translate("&cSupport will NOT be offered on outdated versions."));
                            bukkitCommandSender.sendMessage(CC.translate(""));
                            bukkitCommandSender.sendMessage(CC.translate("&6New Version&7:&r " + res[2]));
                        }
                        bukkitCommandSender.sendMessage(CC.translate("&6Debug Mode: " + instance.displayDebugStatus()));
                        bukkitCommandSender.sendMessage(CC.translate("&6Author&7:&r DevDipin, LeandroSSJ"));
                        bukkitCommandSender.sendMessage(CC.translate("&6Current Version&7:&r " + instance.getDescription().getVersion()));
                        bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
                        return true;
                    } else {
                        bukkitCommandSender.sendMessage(CC.translate("&cTheir was an error while verifying your license."));
                        bukkitCommandSender.sendMessage(CC.translate(""));
                        bukkitCommandSender.sendMessage(CC.translate("&7\u2605 &6Error Message&7:&r " + res[0]));
                        bukkitCommandSender.sendMessage(CC.translate("&7\u2605 &6Error Code&7:&r " + res[1]));
                        bukkitCommandSender.sendMessage(CC.translate(""));
                        bukkitCommandSender.sendMessage(CC.translate("&6Create a ticket in Discord!"));
                        bukkitCommandSender.sendMessage(CC.translate("&6Discord:&r helydev.com/discord"));
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&7&m=================================================================================="));
                        return false;
                    }
                } else {
                    bukkitCommandSender.sendMessage(CC.translate("&cTheir was an error while verifying your license."));
                    bukkitCommandSender.sendMessage(CC.translate(""));
                    bukkitCommandSender.sendMessage(CC.translate("&cThe License API is currently offline."));
                    bukkitCommandSender.sendMessage(CC.translate("&cPlease be patient, it will be back available soon."));
                    bukkitCommandSender.sendMessage(CC.translate(""));
                    bukkitCommandSender.sendMessage(CC.translate("&6Create a ticket in Discord!"));
                    bukkitCommandSender.sendMessage(CC.translate("&6Discord:&r helydev.com/discord"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&7&m=================================================================================="));
                    return false;
                }
            } catch (IOException | ParseException e) {
                bukkitCommandSender.sendMessage(CC.translate("&cTheir was an error while verifying your license."));
                bukkitCommandSender.sendMessage(CC.translate(""));
                bukkitCommandSender.sendMessage(CC.translate("&7\u2605 &6Error&7:&r " + e.getMessage()));
                bukkitCommandSender.sendMessage(CC.translate(""));
                bukkitCommandSender.sendMessage(CC.translate("&6Create ticket in Discord!"));
                bukkitCommandSender.sendMessage(CC.translate("&6Discord:&r helydev.com/discord"));
                Bukkit.getConsoleSender().sendMessage(CC.translate("&7&m=================================================================================="));
                return false;
            }
        }

        private String createJSONData(String javaVersion, String hwid, String os) {
            return "{\"licensekey\":\"" + license + "\","
                    + "\"product\":\"" + product + "\","
                    + "\"version\":\"" + version + "\","
                    + "\"java\":\"" + javaVersion + "\","
                    + "\"hwid\":\"" + hwid + "\","
                    + "\"OS\":\"" + os + "\"}";
        }

        private HttpURLConnection createConnection() throws IOException {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("authorization", apiKey);
            connection.setDoOutput(true);
            return connection;
        }


        private void sendData(HttpURLConnection connection, String postData) throws IOException {
            try (OutputStream out = connection.getOutputStream()) {
                byte[] input = postData.getBytes("utf-8");
                out.write(input, 0, input.length);
            }
        }

        private String[] handleResponse(HttpURLConnection connection) throws IOException, ParseException {
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) return new String[]{ String.valueOf(responseCode) };

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonResponse = response.toString();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject;
            try {
                jsonObject = (JSONObject) parser.parse(jsonResponse);
            } catch (org.json.simple.parser.ParseException e) {
                return null;
            }

            // Extract and print values from the JSON response
            String message = (String) jsonObject.get("message");
            String code = (String) jsonObject.get("code");
            String lastVersion = (String) jsonObject.get("latestVersion");

            String expectedSocket = createExpectedSocket(product, license);
            if (message == null || code == null) return new String[]{"Error"};

            if (!code.equals("000")) return new String[]{ message, code };
            
            String socket = decodeSocket((String) jsonObject.get("socket"));
            if (socket == null || !expectedSocket.equals(socket)) return new String[]{ message, code };

            if (lastVersion == null) return new String[]{ message, code };
            return new String[]{ message, code, lastVersion };
        }


        private String decodeSocket(String base64Socket) throws UnsupportedEncodingException {
            byte[] socketBytes = Base64.getDecoder().decode(base64Socket);
            return new String(socketBytes, "utf-16le");
        }

        private String createExpectedSocket(String product, String license) {
            String expectedSocket = product + System.currentTimeMillis();
            expectedSocket = expectedSocket.substring(0, expectedSocket.length() - 5)
                    + license.substring(0, license.length() - 7);
            return expectedSocket;
        }

        // Función para obtener el HWID
        private String getHWID() {
            StringBuilder hwidBuilder = new StringBuilder();

            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface iface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = iface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                            byte[] macAddress = iface.getHardwareAddress();
                            if (macAddress != null) {
                                for (byte b : macAddress) {
                                    hwidBuilder.append(String.format("%02X", b));
                                }
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }

            return hwidBuilder.toString();
        }
    }
}
