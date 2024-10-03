package net.ascend.core.bukkit.profile.log;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.*;

@Getter
@Setter
public class Log {

    private UUID userUUID;
    private String userName;
    private Integer id;
    private String server;
    private List<String> message;
    private LogType logType;
    private LogCategories logCategory;
    private Long date;
    private boolean loaded = false;

    public Log(Integer id, UUID userUUID, String server, List<String> message, Long date, String logCategory) {
        this.id = id;
        this.userUUID = userUUID;
        this.server = server;
        this.message = message;
        this.logCategory = LogCategories.valueOf(logCategory);
        this.date = date;
    }

    public void save() {
        Document document = new Document();
        document.put("userUUID", userUUID.toString());
        document.put("userName", userName);
        document.put("server", server);
        Type messageListType = new TypeToken<List<String>>() {}.getType();
        document.put("message", XenonBukkit.PLAIN_GSON.toJson(this.message, messageListType));
        document.put("logDate", date);
        document.put("logType", logType.toString());
        document.put("logCategory", logCategory.toString());
        document.put("logID", id);

        XenonBukkit.getInstance().getMongoModule().getLogsCollection().replaceOne(Filters.eq("logID", id.toString()), document, new ReplaceOptions().upsert(true));
    }

    public List<String> formatMessage(List<String> message) {
        List<String> finalMessage = new ArrayList<>();

        for (String string : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("logs-menu.button.first_lore")) {
            string = string.replace("%player_name%", userName)
                    .replace("%server%", server)
                    .replace("%date%", TimeUtil.formatIntoCalendarString(new Date(date)));

            finalMessage.add(string);
        }

        for (String messages : message) {
            finalMessage.add(messages);
        }

        finalMessage.add(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("logs-menu.button.last_lore"));

        return finalMessage;
    }
}
