package dev.hely.core.bukkit.tag;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.lib.util.CC;
import lombok.*;
import org.bson.Document;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Tag {

    private final String name;

    private String prefix = "";
    private int weight;

    private OxygenBukkit plugin = OxygenBukkit.getInstance();

    public void save() {
        Document document = new Document();
        document.put("name", this.name);
        document.put("prefix", this.prefix);
        document.put("weight", this.weight);

        plugin.getMongoModule().getTagsCollection().replaceOne(Filters.eq("name", name), document, new UpdateOptions().upsert(true));
    }

    public void load() {
        Document document = plugin.getMongoModule().getTagsCollection().find(Filters.eq("name", name)).first();

        if (document != null) {
            setPrefix(CC.translate(document.getString("prefix")));
            setWeight(document.getInteger("weight"));
        }
    }

    public void remove() {
        plugin.getTagManager().getTags().remove(this);
        plugin.getMongoModule().getTagsCollection().deleteOne(Filters.eq("name", this.name));
    }
}
