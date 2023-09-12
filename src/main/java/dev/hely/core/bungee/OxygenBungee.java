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
import dev.hely.core.bungee.databases.mongo.MongoManager;
import dev.hely.core.bungee.listener.PacketListener;
import dev.hely.core.bungee.listener.PermissionListener;
import dev.hely.core.bungee.maintenance.MaintenanceManager;
import dev.hely.core.bungee.packet.PermissionPacketListener;
import dev.hely.core.bungee.databases.redis.RedisBungeeModule;
import dev.hely.core.bungee.utils.BungeeCC;
import dev.hely.core.bungee.utils.BungeeConfig;
import dev.hely.lib.CC;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.*;
import java.net.*;
import java.text.ParseException;
import java.io.*;

@Getter
public class OxygenBungee extends Plugin {
    @Getter private static OxygenBungee instance;
    private boolean debug_mode;

    //Configs:
    private BungeeConfig databaseConfig;
    private BungeeConfig bungeeConfig;
    private BungeeConfig languageConfig;
    private BungeeConfig serverConfig;
    private BungeeConfig licenseConfig;

    private RedisBungeeModule redisBungeeModule;
    private List<ModuleLoader> modules;
    private MaintenanceManager maintenanceManager;
    private MongoManager mongoManager;

    private final Map<UUID, List<String>> playerPermissions = new HashMap<>();
    public static final Gson PLAIN_GSON = (new GsonBuilder()).serializeNulls().create();

    @Override
    public void onEnable() {
        instance = this;

        this.licenseConfig = new BungeeConfig(this, "license");

        boolean verify = new SkyLicense("http://207.244.252.245:5555/api/license", this.licenseConfig.getConfiguration().getString("license")).verify();
        if (!verify) {
            this.onDisable();
            return;
        }

        //this.startupMessages();
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
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        modules.forEach(this::unloadModule);
    }


    public void initModules(){
        redisBungeeModule = new RedisBungeeModule();
        maintenanceManager = new MaintenanceManager();
        mongoManager = new MongoManager();
    }

    public void setupModules(){
        modules = new ArrayList<>();

        this.loadModule(maintenanceManager);
        this.loadModule(mongoManager);

        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered &a&n" + modules.size() + " Modules&a."));
    }

    public void setupRunnables() {
        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered all runnables."));
    }

    public void setupPackets() {
        redisBungeeModule.registerListener(new PermissionPacketListener());

        redisBungeeModule.subscribeJedis();

        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered all packets."));
    }

    public void setupCommands() {
        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6[Oxygen] &aSuccessfully registered all commands."));
    }

    public void setupListeners() {
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
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Attempting to verify your license.."));
            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));


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
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Successfully verified your license key.."));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Loading plugin.."));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6&lOxygen Rank Core"));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                        if (!version.equals(res[2])) {
                            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&cThis plugin is outdated!"));
                            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&cPlease download the new updated version in our discord."));
                            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&cSupport will NOT be offered on outdated versions."));
                            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                            OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6New Version&7:&r " + res[2]));
                        }
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Debug Mode: " + instance.displayDebugStatus()));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Author&7:&r DevDipin, LeandroSSJ"));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Current Version&7:&r " + instance.getDescription().getVersion()));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
                        return true;
                    } else {
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&cTheir was an error while verifying your license."));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7\u2605 &6Error Message&7:&r " + res[0]));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7\u2605 &6Error Code&7:&r " + res[1]));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Create a ticket in Discord!"));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Discord:&r helydev.com/discord"));
                        OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
                        return false;
                    }
                } else {
                    OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&cTheir was an error while verifying your license."));
                    OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                    OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&cThe License API is currently offline."));
                    OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&cPlease be patient, it will be back available soon."));
                    OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                    OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Create a ticket in Discord!"));
                    OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Discord:&r helydev.com/discord"));
                    OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
                    return false;
                }
            } catch (IOException | ParseException e) {
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&cTheir was an error while verifying your license."));
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7\u2605 &6Error&7:&r " + e.getMessage()));
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate(""));
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Create ticket in Discord!"));
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&6Discord:&r helydev.com/discord"));
                OxygenBungee.getInstance().getLogger().info(BungeeCC.translate("&7&m=================================================================================="));
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