package dev.hely.core.utils;


import com.google.gson.*;
import dev.hely.core.Oxygen;
import dev.hely.lib.CC;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.*;

import javax.net.ssl.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class PluginUtils {

    public static String SERVER_VERSION_PACKAGE;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        SERVER_VERSION_PACKAGE = packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public static boolean isInteger(String index) {
        try {
            Integer.parseInt(index);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            players.add(player);
        }
        return players;
    }

    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 2F, 2F);
    }

    public static void playSound(Player player, Sound sound, float v, float v2) {
        player.playSound(player.getLocation(), sound, v, v2);
    }

    public static void playSound(Player player, String sound) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf(sound), 2F, 2F);
        } catch (Exception ignored) {
        }
    }

    public static int getPing(Player who) {
        try {
            String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle").invoke(who);
            return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception ignored) {
            return -1;
        }
    }

    public static void sendPlayer(Player player, String server, JavaPlugin plugin) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }

    public static String formatTime(int timer) {
        int hours = timer / 3600;
        int secondsLeft = timer - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";

        if (hours > 0) {
            if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";
        }

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds;

        return formattedTime;
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&c[&cLog&c] " + message));
    }

    public static void logMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }

    public static boolean isJson(Oxygen plugin, String source, Type type) {
        try {
            Oxygen.PLAIN_GSON.fromJson(source, type);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }

    public static String randomString() {
        StringBuilder stringBuilder = new StringBuilder();

        IntStream.range(0, 9).forEach(i -> {
            int random = ThreadLocalRandom.current().nextInt(52);
            char base = (random < 26) ? 'A' : 'a';
            stringBuilder.append((char) (base + random % 26));
        });

        return stringBuilder.toString();
    }

    public static String randomString(int bound) {
        StringBuilder stringBuilder = new StringBuilder();

        IntStream.range(0, bound).forEach(i -> {
            int random = ThreadLocalRandom.current().nextInt(52);
            char base = (random < 26) ? 'A' : 'a';
            stringBuilder.append((char) (base + random % 26));
        });

        return stringBuilder.toString();
    }

    public static int getPunishPriority(Player player) {
        for (int i = 50; i > 0; i--) {
            if (player.hasPermission("aqua.punish.priority." + i)) return i;
        }
        return 1;
    }

    public static int getMaxEnchantLevel(Player player) {
        if (player.hasPermission("aqua.enchant.level.bypass")) return 9999;
        if (player.hasPermission("aqua.enchant.level.hard")) return 21;
        if (player.hasPermission("aqua.enchant.level.medium")) return 14;
        if (player.hasPermission("aqua.enchant.level.easy")) return 7;

        return 5;
    }

    public static Enchantment getEnchantmentByName(Object object) {
        String value = object.toString().replace("_", "").trim();

        for (Enchantment enchant : Enchantment.values()) {
            if (value.equals(String.valueOf(enchant.getId()))
                    || value.equalsIgnoreCase(enchant.getName().replace("_", ""))
                    || value.equalsIgnoreCase(enchant.getName())) {
                return enchant;
            }
        }

        switch (value.toUpperCase()) {
            case "PROT":
            case "PROTECTION":
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "UNB":
            case "UNBREAKING":
                return Enchantment.DURABILITY;
            case "FIREP":
            case "FP":
            case "FIREPROTECTION":
                return Enchantment.PROTECTION_FIRE;
            case "FEATHERF":
            case "FL":
            case "FEATHERFALLING":
                return Enchantment.PROTECTION_FALL;
            case "BLASTP":
            case "BP":
            case "BLASTPROTECTION":
                return Enchantment.PROTECTION_EXPLOSIONS;
            case "SHARP":
            case "SHARPNESS":
                return Enchantment.DAMAGE_ALL;
            case "KNOCK":
            case "KNOCKBACK":
                return Enchantment.KNOCKBACK;
            case "FIREA":
            case "FA":
            case "FIRE":
            case "FIREASPECT":
                return Enchantment.FIRE_ASPECT;
            case "L":
            case "LOOT":
            case "LOOTING":
                return Enchantment.LOOT_BONUS_MOBS;
            case "F":
            case "FORT":
            case "FORTUNE":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "ST":
            case "SILK":
            case "SILKTOUCH":
                return Enchantment.SILK_TOUCH;
            case "EFF":
            case "EFFICIENCY":
                return Enchantment.DIG_SPEED;
            case "SM":
            case "SMITE":
                return Enchantment.DAMAGE_UNDEAD;
            case "INF":
            case "INFINITY":
                return Enchantment.ARROW_INFINITE;
            case "FLA":
            case "FLAME":
                return Enchantment.ARROW_FIRE;
            case "PUNCH":
                return Enchantment.ARROW_KNOCKBACK;
            case "POWER":
                return Enchantment.ARROW_DAMAGE;
            default:
                return null;
        }
    }

    public static String postHastebin(String text, boolean raw) throws IOException {
        byte[] postData = text.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;

        String requestURL = "https://hastebin.com/documents";
        URL url = new URL(requestURL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Hastebin Java Api");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);

        String response = null;
        DataOutputStream wr;
        try {
            wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);

            String postURL = raw ? "https://hastebin.com/raw/" : "https://hastebin.com/";
            response = postURL + response;
        }

        return response;
    }

    public static void setSlots(int slots, JavaPlugin plugin) {
        slots = Math.abs(slots);

        try {
            Object invoke = Class.forName(String.valueOf(Bukkit.getServer().getClass().getPackage().getName()) + ".CraftServer").getDeclaredMethod("getHandle", (Class<?>[]) new Class[0]).invoke(Bukkit.getServer(), new Object[0]);
            Field declaredField = invoke.getClass().getSuperclass().getDeclaredField("maxPlayers");

            declaredField.setAccessible(true);
            declaredField.set(invoke, slots);

            changeProperties(slots, plugin);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void changeProperties(int slots, JavaPlugin plugin) {
        Path resolve = Paths.get(plugin.getDataFolder().getParentFile().getAbsolutePath(), new String[0]).getParent().resolve("server.properties");

        try {
            List<String> allLines = Files.readAllLines(resolve);

            for (int i = 0; i < allLines.size(); ++i) {
                if (allLines.get(i).startsWith("max-players")) {
                    allLines.remove(i);
                }
            }

            allLines.add("max-players=" + slots);

            Files.write(resolve, allLines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
