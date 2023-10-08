package dev.hely.core.bukkit.tag;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.tag.menu.data.action.CustomButtonAction;
import dev.hely.core.bukkit.tag.menu.data.button.CustomButton;
import dev.hely.core.bukkit.tag.menu.data.category.CustomCategory;
import dev.hely.lib.maker.ItemMaker;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
public class TagManager implements ModuleLoader {
    private boolean enabled;
    private List<Tag> tags = new ArrayList<>();
    private List<CustomButton> customButtonStorage;
    private List<CustomCategory> customCategoriesStorage;
    private OxygenBukkit plugin;

    @Override
    public void enable() {
        this.plugin = OxygenBukkit.getInstance();
        setupTags();
        
        this.customButtonStorage = new ArrayList<>();
        this.customCategoriesStorage = new ArrayList<>();

        loadMainMenuButtons();
        loadCategories();
        loadCategoriesButtons();
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


    public void loadMainMenuButtons() {
        this.customButtonStorage.clear();

        FileConfiguration file = OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration();

        for (String key : file.getConfigurationSection("category-menu.buttons").getKeys(false)) {
            Material material = Material.BOOK;
            try {
                material = Material.valueOf(file.getString("category-menu.buttons." + key + ".item.material"));
            } catch (Exception e) {
                OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe material of '&b" + key + "&c' is invalid.");
            }

            int data = file.getInt("category-menu.buttons." + key + ".item.data");
            int amounts = file.getInt("category-menu.buttons." + key + ".item.amount");
            int slot = file.getInt("category-menu.buttons." + key + ".slot");
            String name = file.getString("category-menu.buttons." + key + ".name");
            List<String> lore = file.getStringList("category-menu.buttons." + key + ".lore");
            CustomButtonAction actions = CustomButtonAction.NONE;
            try {
                actions = CustomButtonAction.valueOf(file.getString("category-menu.buttons." + key + ".action"));
            } catch (Exception e2) {
                OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe action for '&b" + key + "&c' is invalid.");
            }
            String category = file.getString("category-menu.buttons." + key + ".settings.sorted.category.name");

            int amount = file.getInt("category-menu.buttons." + key + ".amount");
            ItemStack itemStack = ItemMaker.of(material).setData((short) data).setAmount(amounts).setDisplayName(name).setLore(lore).build();

            CustomButton button = new CustomButton();
            button.setButton(itemStack);
            button.setSlot(slot);
            button.setActions(actions);
            button.setOwnsLore(lore);
            button.setNoPermsLore(lore);
            button.setAmount(amount);
            button.setSubMenu(category);

            this.customButtonStorage.add(button);
        }
        OxygenBukkit.getInstance().debug_message("&cSuccessfully loaded &b" + this.customButtonStorage.size() + " Tags Custom Buttons&c!");

    }
    public void loadCategories() {
        this.customCategoriesStorage.clear();

        ConfigurationSection section = OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getConfigurationSection("category-menu.categories");

        section.getKeys(false).forEach(key -> {
            List<CustomButton> customButtons = new ArrayList<>();
            String title = section.getString(key + ".title");
            int rows = section.getInt(key + ".rows");
            boolean fill_toggle = section.getBoolean(key + ".fill.enabled");
            int fill_data = section.getInt(key + ".fill.data");
            String fill_style = section.getString(key + ".fill.style");
            List<String> fill_corners = section.getStringList(key + ".fill.slots");
            boolean fill_glow = section.getBoolean(key + ".fill.glow");

            boolean back_toggle = section.getBoolean(key + ".back.enabled");
            String back_material = section.getString(key + ".back.item.material");
            int back_data = section.getInt(key + ".back.item.data");
            String back_name = section.getString(key + ".back.item.name");
            List<String> back_lore = section.getStringList(key + ".back.item.lore");
            int back_slot = section.getInt(key + ".back.slot");

            boolean reset_toggle = section.getBoolean(key + ".reset-tag.enabled");
            String reset_material = section.getString(key + ".reset-tag.item.material");
            int reset_data = section.getInt(key + ".reset-tag.item.data");
            String reset_name = section.getString(key + ".reset-tag.item.name");
            List<String> reset_lore = section.getStringList(key + ".reset-tag.item.lore");
            int reset_slot = section.getInt(key + ".reset-tag.slot");

            this.customCategoriesStorage.add(new CustomCategory(title, key, customButtons, rows, fill_toggle, fill_data, fill_style, fill_corners, fill_glow, back_toggle, back_material, back_data, back_name, back_lore, back_slot, reset_toggle, reset_material, reset_data, reset_name, reset_lore, reset_slot));
        });

        OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cSuccessfully loaded &b" + this.customCategoriesStorage.size() + " Custom Categories&c!");
    }
    public void loadCategoriesButtons() {
        for (CustomCategory category : this.customCategoriesStorage) {
            ConfigurationSection section = OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getConfigurationSection("category-menu.categories." + category.getName() + ".buttons");

            section.getKeys(false).forEach(key -> {
                Material material = Material.BOOK;
                try {
                    material = Material.valueOf(section.getString(key + ".item.material"));
                } catch (Exception e) {
                    OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe material of '&b" + category.getName() + "-" + key + "&c' is invalid.");
                }
                int data = section.getInt(key + ".item.data");
                int amounts = section.getInt(key + ".item.amount");
                String name = section.getString(key + ".item.name");
                List<String> ownsLore = section.getStringList(key + ".item.lores.owns");
                List<String> noPermsLore = section.getStringList(key + ".item.lores.no-permission");
                int slot = section.getInt(key + ".slot");
                String tagName = section.getString(key + ".tag-name");
                CustomButtonAction actions = CustomButtonAction.NONE;
                try {
                    actions = CustomButtonAction.valueOf(section.getString(key + ".actions"));
                } catch (Exception e2) {
                    OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe action for '&b" + key + "&c' is invalid.");
                }
                int amount = section.getInt(key + ".amount");

                ItemStack itemStack = ItemMaker.of(material)
                        .setData((short) data)
                        .setAmount(amounts)
                        .setDisplayName(name)
                        .setLore(ownsLore)
                        .build();

                CustomButton button = new CustomButton();
                button.setButton(itemStack);
                button.setSlot(slot);
                button.setOwnsLore(ownsLore);
                button.setNoPermsLore(noPermsLore);
                button.setAmount(amount);
                button.setTagName(tagName);
                button.setActions(actions);

                category.getCustomButtons().add(button);
            });
        }
    }

    public CustomCategory getCategoryByName(String name) {
        for (CustomCategory customCategory : customCategoriesStorage) {
            if (customCategory.getName().equalsIgnoreCase(name)) {
                return customCategory;
            }
        }
        return null;
    }
}
