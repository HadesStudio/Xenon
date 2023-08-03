package dev.hely.core.tag;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hely.core.Oxygen;
import dev.hely.lib.CC;
import lombok.*;
import org.bson.Document;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Tag {

    private final String name;

    private String prefix = "";
    private int weight;

    private Oxygen plugin = Oxygen.getInstance();

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
