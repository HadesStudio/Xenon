package net.ascend.core.bukkit.profile.log;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import lombok.Getter;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.util.TimeUtil;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogManager implements ModuleLoader {

    private static final File directory = new File(new File("xenon-logs"), "playerLogs");

    @Getter private final Map<Integer, Log> logMap = new HashMap<>();

    private boolean saveFolder;

    @Override
    public void onEnable() {
        registerAllLogs();

        saveFolder = XenonBukkit.getInstance().getConfiguration().getConfiguration().getBoolean("log.save-in-folder");
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {

    }

    public void registerAllLogs() {
        for (Document document : XenonBukkit.getInstance().getMongoModule().getLogsCollection().find()) {
            Integer id = document.getInteger("logID");
            Type messageListType = new TypeToken<List<String>>() {}.getType();
            Log log = new Log(id,
                    UUID.fromString(document.getString("userUUID")),
                    document.getString("server"),
                    (XenonBukkit.PLAIN_GSON.fromJson(document.getString("message"), messageListType)),
                    document.getLong("logDate"),
                    document.getString("logCategory"));

            log.setUserName(document.getString("userName"));
            log.setLogType(LogType.valueOf(document.getString("logType")));
            this.logMap.put(id, log);
        }
    }


    public void logPlayerAction(UUID playerUUID, String userName, List<String> message, LogType type, LogCategories logCategories) {
        Bukkit.getScheduler().runTaskAsynchronously(XenonBukkit.getInstance(), () -> {
            String server = XenonBukkit.getInstance().getServerManager().getCurrentServerName();
            Log log = new Log((this.logMap.size() + 1), playerUUID,
                    server,
                    message,
                    System.currentTimeMillis(),
                    logCategories.toString());

            log.setLogType(type);
            log.setUserName(userName);
            log.save();

            logMap.put(log.getId(), log);

            if (saveFolder) {
                logToFile(playerUUID, userName, server, message, type);
            }
        });
    }

    private static void logToFile(UUID playerUUID, String userName, String server, List<String> message, LogType logType) {
        File logFolder = new File(directory, playerUUID.toString());
        File logFile = new File(logFolder, "general.log");
        try {
            StringBuilder logLine = new StringBuilder();

            logLine.append('[');
            logLine.append(TimeUtil.formatIntoCalendarString(new Date(System.currentTimeMillis())));
            logLine.append(" @ ");
            logLine.append(server);
            logLine.append("] (");
            logLine.append(logType);
            logLine.append(") ");

            logLine.append(userName);
            logLine.append(": ");


            for (String line : message) {
                logLine.append(ColorUtil.removeColorCodes(line));
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
            Logger.getLogger(LogManager.class.getName()).log(Level.SEVERE, "Failed to log to file for player: " + playerUUID, ex);

        }
    }

    public Log getLogById(int id) {
        return Optional.ofNullable(this.logMap.get(id)).orElse(new Log(id, null, "Unknown", Collections.singletonList("Unknown"), System.currentTimeMillis(), null));
    }
}