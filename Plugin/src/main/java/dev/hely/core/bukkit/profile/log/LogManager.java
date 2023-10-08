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
import com.google.gson.reflect.TypeToken;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.log.category.LogCategories;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class LogManager {
    private static final File directory = new File(new File("oxygen-logs"), "playerLogs");

    @Getter private final Map<Integer, Log> logMap = new HashMap<>();

    public Log getLogById(int id) {
        return this.logMap.get(id) == null ? new Log(id, null, Collections.singletonList("Unkonw"), System.currentTimeMillis(), null) : this.logMap.get(id);
    }

    public void registerAllLogs() {
        long startTime = System.currentTimeMillis();
        int size = 0;
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getLogsCollection().find()) {
            Integer id = document.getInteger("logID");
            Type messageListType = new TypeToken<List<String>>() {}.getType();
            Log log = new Log(id, UUID.fromString(document.getString("userUUID")), (OxygenBukkit.PLAIN_GSON.fromJson(document.getString("message"), messageListType)), document.getLong("logDate"), document.getString("logCategory"));
            log.setUserName(document.getString("userName"));
            log.setLogType(LogActionTypes.valueOf(document.getString("logType")));
            size = size + 1;
            this.logMap.put(id, log);
        }
        startTime = startTime - System.currentTimeMillis();
    }


    public void logPlayerAction(UUID playerUUID, String userName, List<String> message, LogActionTypes type, LogCategories logCategories) {
        Bukkit.getScheduler().runTaskAsynchronously(OxygenBukkit.getInstance(), () -> {
            long startTime = System.currentTimeMillis();
            Log log = new Log((this.logMap.size() + 1), playerUUID, message, System.currentTimeMillis(), logCategories.toString());
            log.setLogType(type);
            log.setUserName(userName);
            log.save();
            logMap.put(log.getId(), log);

            logToFile(playerUUID, userName, message, type);
            startTime = startTime - System.currentTimeMillis();
        });
    }

    private static void logToFile(UUID playerUUID, String userName, List<String> message, LogActionTypes logType) {
        File logFolder = new File(directory, playerUUID.toString());
        File logFile = new File(logFolder, "general.log");
        try {
            StringBuilder logLine = new StringBuilder();

            logLine.append('[');
            logLine.append(userName);
            logLine.append(", ");
            logLine.append(DateTimeFormatter.ISO_INSTANT.format(Instant.now())); // ISO 8601
            logLine.append(", ");
            logLine.append(logType);
            logLine.append("] ");

            for (String line : message) {
                logLine.append("message");
                logLine.append(": ");
                logLine.append(line);
                logLine.append(" ");
            }

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
}