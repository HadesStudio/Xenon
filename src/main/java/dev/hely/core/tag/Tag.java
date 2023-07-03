package dev.hely.core.tag;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hely.core.Oxygen;
import dev.hely.lib.CC;
import lombok.*;
import org.bson.Document;

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

        plugin.getMongoManager().getTagsCollection().replaceOne(Filters.eq("name", name), document, new UpdateOptions().upsert(true));
    }

    public void load() {
        Document document = plugin.getMongoManager().getTagsCollection().find(Filters.eq("name", name)).first();

        if (document != null) {
            this.prefix = CC.translate(document.getString("prefix"));
            this.weight = document.getInteger("weight");
        }
    }

    public void remove() {
        // plugin.getTagManager().getTags().remove(name);
        plugin.getMongoManager().getTagsCollection().deleteOne(Filters.eq("name", this.name));
    }
}
