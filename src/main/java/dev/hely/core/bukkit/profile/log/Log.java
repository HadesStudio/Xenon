package dev.hely.core.bukkit.profile.log;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.log
//   Date: Tuesday, August 22, 2023 - 3:21 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import dev.hely.core.bukkit.utils.TimeUtil;
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
    private final Integer id;
    private List<String> message;
    private LogActionTypes logType;
    private Long date;
    private boolean loaded = false;

    public Log(Integer id, UUID userUUID, List<String> message, Long date) {
        this.id = id;
        this.userUUID = userUUID;
        this.message = message;
        this.date = date;
    }


    public void save() {
        save(false);
    }

    public void save(boolean leave) {
        Document document = new Document();
        document.put("userUUID", userUUID.toString());
        document.put("userName", userName);
        document.put("logID", String.valueOf(id));
        Type messageListType = new TypeToken<List<String>>() {}.getType();
        document.put("message", OxygenBukkit.PLAIN_GSON.toJson(this.message, messageListType));
        document.put("logDate", date);
        document.put("logType", logType.toString());

        OxygenBukkit.getInstance().getMongoModule().getLogsCollection().replaceOne(Filters.eq("logID", id.toString()), document, new ReplaceOptions().upsert(true));
        if (leave) {
            OxygenBukkit.getInstance().getLogManager().getLogMap().remove(id);
        }
    }

    private void load() {
        Document document = OxygenBukkit.getInstance().getMongoModule().getLogsCollection().find(Filters.eq("logID", id.toString())).first();

        if (document != null) {
            userUUID = UUID.fromString(document.getString("userUUID"));
            userName = document.getString("userName");
            Type messageListType = new TypeToken<List<String>>() {}.getType();
            message = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("message"), messageListType);
            date = document.getLong("logDate");
            logType = LogActionTypes.valueOf(document.getString("logType"));
        }

        loaded = true;
    }

    private List<String> formatMessage(List<String> message) {
        List<String> finalMessage = new ArrayList<>();

        finalMessage.add("&7&l&m------------------------");
        finalMessage.add("&6Username&7: &a" + userName);
        finalMessage.add("&6Log Type&7: &a" + logType.getDisplay_name());
        finalMessage.add("&6Date&7: &a" + TimeUtil.formatSimplifiedDateDiff(date));
        finalMessage.add("&7");
        for (String messages : message) {
            finalMessage.add(messages);
        }
        finalMessage.add("&7&l&m------------------------");

        return finalMessage;
    }
}
