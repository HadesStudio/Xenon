package net.ascend.core.bukkit.tag;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.CC;
import lombok.*;
import org.bson.Document;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name Xenon
 */
@Getter
@Setter
public class Tag {

    private final String name;
    private TagType type;
    private String display;
    private String prefix;
    private String permission;
    private int weight, coins;

    private XenonBukkit plugin = XenonBukkit.getInstance();

    public Tag(String name) {
        this.name = name;

        this.type = TagType.NORMAL;
        this.display = "&6Default Tag";
        this.permission = "xenon.tag." + name;
        this.prefix = "&8[&fDefault Tag&8]";
        this.weight = 1;
    }

    public void save() {
        Document document = new Document();
        document.put("name", this.name);
        document.put("type", this.type.name());
        document.put("display", this.display);
        document.put("permission", this.permission);
        document.put("prefix", this.prefix);
        document.put("weight", this.weight);
        document.put("coins", this.coins);

        plugin.getMongoModule().getTagsCollection().replaceOne(Filters.eq("name", name), document, new UpdateOptions().upsert(true));
    }

    public void load() {
        Document document = plugin.getMongoModule().getTagsCollection().find(Filters.eq("name", name)).first();

        if (document != null) {
            setType(TagType.valueOf(document.getString("type")));
            setDisplay(CC.translate(document.getString("display")));
            setPermission(document.getString("permission"));
            setPrefix(CC.translate(document.getString("prefix")));
            setWeight(document.getInteger("weight"));
            setCoins(document.getInteger("coins"));
        }
    }

    public void remove() {
        plugin.getTagManager().getTags().remove(this);
        plugin.getMongoModule().getTagsCollection().deleteOne(Filters.eq("name", this.name));
    }
}
