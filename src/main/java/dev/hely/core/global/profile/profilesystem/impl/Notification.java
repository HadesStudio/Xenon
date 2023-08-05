package dev.hely.core.global.profile.profilesystem.impl;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import dev.hely.core.bukkit.OxygenBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class Notification {
    @Getter private UUID uuid;
    @Getter private long sentdate;
    @Getter private List<String> notification;
}
