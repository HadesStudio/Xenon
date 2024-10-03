package net.ascend.core.bukkit.tag;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name Xenon
 */
@Getter
public class TagManager implements ModuleLoader {

    private XenonBukkit plugin;
    private boolean enabled;

    private List<Tag> tags = new ArrayList<>();

    @Override
    public void onEnable() {
        this.plugin = XenonBukkit.getInstance();

        this.setupTags();
        this.enabled = true;
    }

    @Override
    public void onDisable() {
        saveTags();
        this.enabled = false;
    }

    @Override
    public void onReload() {
        saveTags();
    }

    public void setupTags(){
        this.tags.clear();

        this.plugin.getMongoModule().getTagsCollection().find().into(new ArrayList<>()).forEach(document -> {
            Tag tag = new Tag(document.getString("name"));
            tag.load();

            this.tags.add(tag);
        });
    }

    public void saveTags(){
        this.tags.forEach(Tag::save);
    }

    public void deleteTags(){
        this.tags.clear();
        plugin.getMongoModule().getTagsCollection().drop();
    }


    public Tag getTag(String name) {
        return this.tags.stream().filter(tag -> tag.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
