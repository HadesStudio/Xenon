package net.ascend.core.bukkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.Getter;
import net.ascend.core.bukkit.announcements.AnnouncementManager;
import net.ascend.core.bukkit.chat.ChatModule;
import net.ascend.core.bukkit.client.ClientModule;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.filter.FilterManager;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.modules.database.mongo.MongoModule;
import net.ascend.core.bukkit.modules.database.redis.RedisModule;
import net.ascend.core.bukkit.modules.listener.ListenerModule;
import net.ascend.core.bukkit.placeholder.PlaceHolderAPIExpansion;
import net.ascend.core.bukkit.placeholder.PlaceholderHook;
import net.ascend.core.bukkit.profile.ProfileManager;
import net.ascend.core.bukkit.profile.log.LogManager;
import net.ascend.core.bukkit.profile.notifications.NotificationManager;
import net.ascend.core.bukkit.profile.packet.ProfilePacketListener;
import net.ascend.core.bukkit.profile.squads.SquadManager;
import net.ascend.core.bukkit.profile.tfa.TFAManager;
import net.ascend.core.bukkit.punishment.packet.PunishmentPacketListener;
import net.ascend.core.bukkit.rank.grant.packets.listener.GrantPacketListener;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.ServerListener;
import net.ascend.core.bukkit.server.ServerManager;
import net.ascend.core.bukkit.server.ServerState;
import net.ascend.core.bukkit.server.packet.ServerStateChangePacket;
import net.ascend.core.bukkit.server.queue.Queue;
import net.ascend.core.bukkit.server.queue.QueueManager;
import net.ascend.core.bukkit.server.queue.packet.QueuePacketListener;
import net.ascend.core.bukkit.server.reboot.RebootManager;
import net.ascend.core.bukkit.staff.requests.RequestManager;
import net.ascend.core.bukkit.staff.staffmode.StaffManager;
import net.ascend.core.bukkit.tag.TagManager;
import net.ascend.core.bukkit.tag.packet.TagPacketListener;
import net.ascend.core.bukkit.task.NameMCReminderTask;
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
    private Config grantMenuConfig;
    private Config squadMenuConfig;

    private TaskUtil taskUtil;

    private List<ModuleLoader> modules;
    private GoogleAuthenticator googleAuthenticator;
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
    private TFAManager tfaManager;
    private QueueManager queueManager;
    private Queue queue;
    private LogManager logManager;
    private StaffManager staffManager;
    private AnnouncementManager announcementManager;
    private RebootManager rebootManager;
    private Server serverData;
    private FilterManager filterManager;
    private ChatModule chatManager;
    private NotificationManager notificationManager;
    private PromptManager promptManager;
    private SquadManager squadManager;

    public static final Gson PLAIN_GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();

    public void onEnable() {
        instance = this;
        LAST_START_TIME = System.currentTimeMillis();

        try {
            this.licenseConfig = new Config(this, "license");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        Object[] rest = new SkyLicense("http://207.244.252.245:5555/api/license", this.licenseConfig.getConfiguration().getString("license")).verify();
        if (!rest[0].equals("V") || !(rest[1] instanceof Boolean)) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getScheduler().cancelTasks(this);
            return;
        }

        if (!(Boolean) rest[1]) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getScheduler().cancelTasks(this);
            return;
        }

        setupConfig();
        initModules();
        setupModules();
        setupPackets();
        registerTasks();

        this.logManager.registerAllLogs();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");

        this.serverData = new Server(XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));
        this.serverData.setStartTime(System.currentTimeMillis());
        this.serverData.load();
        this.serverData.save();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHook();
            new PlaceHolderAPIExpansion(this).register();
        }

        this.getRedisModule().sendPacket(new ServerStateChangePacket(serverData.getName(), ServerState.ONLINE));

        this.fullyEnabled = true;
    }

    public void onDisable() {
        if (!this.fullyEnabled) return;

        PlaceholderHook.setENABLED(false);

        try {
            this.getServerData().save();
            this.getRedisModule().sendPacket(new ServerStateChangePacket(serverData.getName(), ServerState.OFFLINE));

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
            this.serverConfig = new Config(XenonBukkit.getInstance(), "server-data");
            this.configList.add(serverConfig);
            this.profileMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "profile-menu");
            this.configList.add(profileMenuConfig);
            this.historyMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "history-menu");
            this.configList.add(historyMenuConfig);
            this.requestsMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "requests-menu");
            this.configList.add(requestsMenuConfig);
            this.logsMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "log-menu");
            this.configList.add(logsMenuConfig);
            this.editProfileMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "edit-profile-menu");
            this.configList.add(editProfileMenuConfig);
            this.tagsMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "tags-menu");
            this.configList.add(tagsMenuConfig);
            this.serversMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "servers-menu");
            this.configList.add(serversMenuConfig);
            this.coinShopConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "coin-shop");
            this.configList.add(coinShopConfig);
            this.staffHistoryConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "staff-history-menu");
            this.configList.add(staffHistoryConfig);
            this.announcementConfig = new Config(XenonBukkit.getInstance(), "announcements");
            this.configList.add(announcementConfig);
            this.vipMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "vip-menu");
            this.configList.add(vipMenuConfig);
            this.grantMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "grant-menu");
            this.configList.add(grantMenuConfig);
            this.squadMenuConfig = new Config(XenonBukkit.getInstance(), "menus" + File.separator + "squads-menu");
            this.configList.add(squadMenuConfig);

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

    public void registerTasks() {
        new NameMCReminderTask().runTaskTimerAsynchronously(XenonBukkit.getInstance(), 20L * 60 * XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("name-mc.reminder-timer"), 20L * 60 * XenonBukkit.getInstance().getConfiguration().getConfiguration().getInt("name-mc.reminder-timer"));
    }

    public void initModules() {
        this.queue = new Queue(this);
        taskUtil = new TaskUtil(XenonBukkit.getInstance());

        redisModule = new RedisModule();
        mongoModule = new MongoModule();
        googleAuthenticator = new GoogleAuthenticator();
        clientManager = new ClientModule();
        commandManager = new CommandManager();
        subCommandManager = new SubCommandManager();
        listenerModule = new ListenerModule();
        rankManager = new RankManager();
        tagManager = new TagManager();
        chatManager = new ChatModule();
        requestHandler = new RequestManager();
        tfaManager = new TFAManager();
        logManager = new LogManager();
        staffManager = new StaffManager();
        announcementManager = new AnnouncementManager();
        notificationManager = new NotificationManager();
        promptManager = new PromptManager(this);

        queueManager = new QueueManager();
        serverManager = new ServerManager();
        rebootManager = new RebootManager();
        filterManager = new FilterManager();
        squadManager = new SquadManager();
    }

    public void setupModules() {
        MenuManager.INSTANCE.onEnable(XenonBukkit.getInstance());
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
        this.loadModule(notificationManager);
        this.loadModule(tfaManager);
        this.loadModule(staffManager);
        this.loadModule(filterManager);
        this.loadModule(serverManager);
        this.loadModule(announcementManager);
        this.loadModule(squadManager);
    }

    public void setupPackets() {
        redisModule.registerListener(new ProfilePacketListener());
        redisModule.registerListener(new PunishmentPacketListener(this));
        redisModule.registerListener(new TagPacketListener(tagManager));
        redisModule.registerListener(new GrantPacketListener());
        redisModule.registerListener(new ServerListener());
        redisModule.registerListener(new QueuePacketListener());

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

    public class SkyLicense {
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

        public Object[] verify() {
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
                        bukkitCommandSender.sendMessage(CC.translate("&6&lXenon (Rank Core)"));
                        bukkitCommandSender.sendMessage(CC.translate(""));

                        if (!version.equals(res[2])) {
                            bukkitCommandSender.sendMessage(CC.translate("&cThis plugin is outdated!"));
                            bukkitCommandSender.sendMessage(CC.translate("&cPlease download the new updated version in our discord."));
                            bukkitCommandSender.sendMessage(CC.translate("&cSupport will NOT be offered on outdated versions."));
                            bukkitCommandSender.sendMessage(CC.translate(""));
                            bukkitCommandSender.sendMessage(CC.translate("&6New Version&7:&r " + res[2]));
                        }

                        bukkitCommandSender.sendMessage(CC.translate("&6Author&7:&r DevDipin, LeandroSSJ"));
                        bukkitCommandSender.sendMessage(CC.translate("&6Current Version&7:&r " + instance.getDescription().getVersion()));
                        bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
                        return new Object[]{"V", true};
                    } else {
                        bukkitCommandSender.sendMessage(CC.translate("&cTheir was an error while verifying your license."));
                        bukkitCommandSender.sendMessage(CC.translate(""));
                        bukkitCommandSender.sendMessage(CC.translate("&7\u2605 &6Error Message&7:&r " + res[0]));
                        bukkitCommandSender.sendMessage(CC.translate("&7\u2605 &6Error Code&7:&r " + res[1]));
                        bukkitCommandSender.sendMessage(CC.translate(""));
                        bukkitCommandSender.sendMessage(CC.translate("&6Create a ticket in Discord!"));
                        bukkitCommandSender.sendMessage(CC.translate("&6Discord:&r ascendproyect.org/discord"));
                        bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
                        return new Object[]{"I", 135};
                    }
                } else {
                    bukkitCommandSender.sendMessage(CC.translate("&cTheir was an error while verifying your license."));
                    bukkitCommandSender.sendMessage(CC.translate(""));
                    bukkitCommandSender.sendMessage(CC.translate("&cThe License API is currently offline."));
                    bukkitCommandSender.sendMessage(CC.translate("&cPlease be patient, it will be back available soon."));
                    bukkitCommandSender.sendMessage(CC.translate(""));
                    bukkitCommandSender.sendMessage(CC.translate("&6Create a ticket in Discord!"));
                    bukkitCommandSender.sendMessage(CC.translate("&6Discord:&r ascendproyect.org/discord"));
                    bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
                    return new Object[]{"I", 135};
                }
            } catch (IOException | ParseException e) {
                bukkitCommandSender.sendMessage(CC.translate("&cTheir was an error while verifying your license."));
                bukkitCommandSender.sendMessage(CC.translate(""));
                bukkitCommandSender.sendMessage(CC.translate("&7\u2605 &6Error&7:&r " + e.getMessage()));
                bukkitCommandSender.sendMessage(CC.translate(""));
                bukkitCommandSender.sendMessage(CC.translate("&6Create ticket in Discord!"));
                bukkitCommandSender.sendMessage(CC.translate("&6Discord:&r ascendproyect.org/discord"));
                bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
                return new Object[]{"I", 135};
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
