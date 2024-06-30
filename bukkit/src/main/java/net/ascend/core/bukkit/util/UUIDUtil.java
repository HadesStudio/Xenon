package net.ascend.core.bukkit.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
public class UUIDUtil {
    private static final Gson GSON = new GsonBuilder().create();
    private static Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0" +
            "-9a-fA-F]{3}-[0-9a-fA-F]{12}");

    public static String fetchName(UUID uuid) {
        String formattedUUID = uuid.toString().replace("-", "");

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + formattedUUID);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String textResponse = bufferedReader.lines().collect(Collectors.joining(" "));

            Map<?, ?> map = GSON.fromJson(textResponse, Map.class);
            return map.get("name").toString();

        } catch (Exception exception) {
            return null;
        }
    }

    public static boolean isUUID(String string) {
        return UUID_PATTERN.matcher(string).find();
    }

    public static UUID fetchUUID(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);

            JsonObject json = (new JsonParser()).parse(new InputStreamReader(url.openStream())).getAsJsonObject();
            String uuid = json.get("id").toString().replace("\"", "");
            return formatUUID(uuid);
        } catch (Exception exception) {
            return null;
        }
    }

    public static UUID formatUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
    }
}