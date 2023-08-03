package dev.hely.core.global.tag;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name OxygenGlobal
 */
public class TagManager implements ModuleLoader {
    private boolean enabled;
    @Getter private List<Tag> tags = new ArrayList<>();
    private OxygenBukkit plugin;

    @Override
    public void enable() {
        this.plugin = OxygenBukkit.getInstance();

        setupTags();
        this.enabled = true;
    }

    @Override
    public void disable() {
        saveTags();
        this.enabled = false;
    }

    @Override
    public void reload() {
        saveTags();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Tag";
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
