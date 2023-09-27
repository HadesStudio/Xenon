package dev.hely.core.bukkit.tag;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.profilesystem.impl.Preferences;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.Statistics;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.StatisticsServer;
import dev.hely.core.bukkit.profile.utils.PlaytimeUtil;
import dev.hely.lib.util.CC;
import lombok.*;
import org.bson.Document;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
@Setter
public class Tag {
    private final String name;
    private String display;
    private String prefix;
    private String permission;
    private int weight;

    private OxygenBukkit plugin = OxygenBukkit.getInstance();


    public Tag(String name) {
        this.name = name;

        this.display = "&6Oxygen Default Tag";
        this.permission = "oxygen.tag." + name;
        this.prefix = "&6[Oxygen Default Tag]";
        this.weight = 1;

        save();
    }

    public void save() {
        Document document = new Document();
        document.put("name", this.name);
        document.put("display", this.display);
        document.put("permission", this.permission);
        document.put("prefix", this.prefix);
        document.put("weight", this.weight);

        plugin.getMongoModule().getTagsCollection().replaceOne(Filters.eq("name", name), document, new UpdateOptions().upsert(true));
    }

    public void load() {
        Document document = plugin.getMongoModule().getTagsCollection().find(Filters.eq("name", name)).first();

        if (document != null) {
            setPermission(document.getString("permission"));
            setDisplay(CC.translate(document.getString("display")));
            setPrefix(CC.translate(document.getString("prefix")));
            setWeight(document.getInteger("weight"));
        }
    }

    public void remove() {
        plugin.getTagManager().getTags().remove(this);
        plugin.getMongoModule().getTagsCollection().deleteOne(Filters.eq("name", this.name));
    }
}
