package dev.hely.core.profile.log;

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
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import dev.hely.core.profile.log.types.LogActionTypes;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        Bukkit.getScheduler().runTaskAsynchronously(Oxygen.getInstance(), () -> {
            System.out.print("----------------------------------");
            System.out.print("Attemtping to log action to file:");
            System.out.print("");
            System.out.print("Player UUID - " + playerUUID);
            System.out.print("Type - " + type);
            System.out.print("Parameters - " + params);
            System.out.print("");
            logToFile(playerUUID, type, params);
            System.out.print("Done?");
            System.out.print("----------------------------------");

            if (type.isLogging()) {
                System.out.print("----------------------------------");
                System.out.print("Attemtping to log action to Mongo database:");
                System.out.print("");
                logToDatabase(playerUUID, type, params);
                System.out.print("Done?");
                System.out.print("----------------------------------");
            }
        });
    }

    private static void logToFile(UUID playerUUID, LogActionTypes type, Map<String, Object> params) {
        File logFolder = new File(directory, playerUUID.toString());
        File logFile = new File(logFolder, "general.log");
        System.out.print(directory);
        System.out.print(logFile);
        try {
            StringBuilder logLine = new StringBuilder();

            logLine.append('[');
            logLine.append(DateTimeFormatter.ISO_INSTANT.format(Instant.now())); // ISO 8601
            logLine.append(", ");
            logLine.append(type.getDisplay_name());
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
        System.out.print("Time - " + new Date());
        document.put("logType", type.getDisplay_name());
        System.out.print("Log Type - " + type.getDisplay_name());
        JsonArray jsonArray = new JsonArray();

        params.forEach((key, value) -> {
            JsonObject info = new JsonObject();

            info.addProperty("key", key);
            info.addProperty("value", value.toString());

            jsonArray.add(info);
        });

        document.put("params", Oxygen.PLAIN_GSON.toJson(jsonArray));

        Oxygen.getInstance().getMongoModule().getPlayerLogsCollection().replaceOne(Filters.eq("playerUUID", playerUUID), document, new ReplaceOptions().upsert(true));
    }
}