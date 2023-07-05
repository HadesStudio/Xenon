package dev.hely.core.tag;

import dev.hely.core.Oxygen;
import dev.hely.core.managers.modules.ModuleLoader;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name Oxygen
 */
public class TagManager implements ModuleLoader {
    private boolean enabled;
    @Getter private List<Tag> tags = new ArrayList<>();
    private Oxygen plugin;

    @Override
    public void enable() {
        this.plugin = Oxygen.getInstance();

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
        return "Tag Manager";
    }

    public void setupTags(){
        this.tags.clear();

        this.plugin.getMongoManager().getTagsCollection().find().into(new ArrayList<>()).forEach(document -> {
            Tag tag = new Tag(document.getString("name"));
            tag.load();

            this.tags.add(tag);
        });
    }

    public void saveTags(){
        this.tags.forEach(Tag::save);
    }

    public Tag getTag(String name) {
        return this.tags.stream().filter(tag -> tag.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
