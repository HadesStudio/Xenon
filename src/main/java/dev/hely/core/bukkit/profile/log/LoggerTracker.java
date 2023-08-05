package dev.hely.core.bukkit.profile.log;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.log
//   Date: Monday, July 31, 2023 - 4:57 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public final class LoggerTracker {
    private static final File directory = new File(new File("oxygen-logs"), "playerLogs");

    public static void logPlayerAction(UUID playerUUID, LogActionTypes type, Map<String, Object> params) {
        Bukkit.getScheduler().runTaskAsynchronously(OxygenBukkit.getInstance(), () -> {
            OxygenBukkit.getInstance().debug_message("----------------------------------");
            OxygenBukkit.getInstance().debug_message("Attemtping to log action to file:");
            OxygenBukkit.getInstance().debug_message("");
            OxygenBukkit.getInstance().debug_message("Player UUID - " + playerUUID);
            OxygenBukkit.getInstance().debug_message("Type - " + type);
            OxygenBukkit.getInstance().debug_message("Parameters - " + params);
            OxygenBukkit.getInstance().debug_message("");
            logToFile(playerUUID, type, params);
            OxygenBukkit.getInstance().debug_message("Done?");
            OxygenBukkit.getInstance().debug_message("----------------------------------");

            if (type.isLogging()) {
                OxygenBukkit.getInstance().debug_message("----------------------------------");
                OxygenBukkit.getInstance().debug_message("Attemtping to log action to Mongo database:");
                OxygenBukkit.getInstance().debug_message("");
                logToDatabase(playerUUID, type, params);
                OxygenBukkit.getInstance().debug_message("Done?");
                OxygenBukkit.getInstance().debug_message("----------------------------------");
            }
        });
    }

    private static void logToFile(UUID playerUUID, LogActionTypes type, Map<String, Object> params) {
        File logFolder = new File(directory, playerUUID.toString());
        File logFile = new File(logFolder, "general.log");
        try {
            StringBuilder logLine = new StringBuilder();

            logLine.append('[');
            logLine.append(DateTimeFormatter.ISO_INSTANT.format(Instant.now())); // ISO 8601
            logLine.append(", ");
            logLine.append(type);
            logLine.append("] ");

            params.forEach((key, value) -> {
                logLine.append(key);
                logLine.append(": ");
                logLine.append(value);
                logLine.append(' ');
            });

            logLine.append('\n');

            logFile.getParentFile().mkdirs();
            logFile.createNewFile();

            Files.append(
                    logLine.toString(),
                    logFile,
                    Charsets.UTF_8
            );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void logToDatabase(UUID playerUUID, LogActionTypes type, Map<String, Object> params) {
        Document document = new Document();

        document.put("time", new Date());
        OxygenBukkit.getInstance().debug_message("Time - " + new Date());
        document.put("logType", type.toString());
        OxygenBukkit.getInstance().debug_message("Log Type - " + type);
        JsonArray jsonArray = new JsonArray();

        params.forEach((key, value) -> {
            JsonObject info = new JsonObject();

            info.addProperty("key", key);
            info.addProperty("value", value.toString());

            jsonArray.add(info);
        });

        document.put("params", OxygenBukkit.PLAIN_GSON.toJson(jsonArray));

        OxygenBukkit.getInstance().getMongoModule().getPlayerLogsCollection().replaceOne(Filters.eq("playerUUID", playerUUID), document, new ReplaceOptions().upsert(true));
    }
}