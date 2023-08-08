package dev.hely.core.bukkit.coins;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins
//   Date: Monday, August 07, 2023 - 6:22 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.categories.Category;
import dev.hely.core.bukkit.coins.item.type.ItemTypes;
import dev.hely.core.bukkit.coins.logging.CurrencyLogging;
import dev.hely.core.bukkit.coins.menu.data.action.CustomButtonAction;
import dev.hely.core.bukkit.coins.menu.data.button.CustomButton;
import dev.hely.core.bukkit.coins.menu.data.category.CustomCategory;
import dev.hely.core.bukkit.coins.sales.Sale;
import dev.hely.core.bukkit.coins.sales.types.SaleTypes;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.lib.maker.ItemMaker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class StoreManager implements ModuleLoader {
    private boolean enabled;
    private final Map<SaleTypes, Sale> activeSales = Maps.newHashMap();
    private final List<Category> categories = Lists.newArrayList();
    private int globalDiscount = 0;
    private List<CustomButton> customButtonStorage;
    private List<CustomCategory> customCategoriesStorage;

    @Override
    public void enable() {
        this.enabled = true;
        this.customButtonStorage = new ArrayList<>();
        this.customCategoriesStorage = new ArrayList<>();

        loadMainMenuButtons();
        loadCategories();
        loadCategoriesButtons();
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Store";
    }

    public void loadMainMenuButtons() {
        this.customButtonStorage.clear();

        FileConfiguration file = OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration();

        for (String key : file.getConfigurationSection("menu.buttons").getKeys(false)) {
            Material material = Material.BOOK;
            try {
                material = Material.valueOf(file.getString("menu.buttons." + key + ".item.material"));
            } catch (Exception e) {
                OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe material of '&b" + key + "&c' is invalid.");
            }

            int data = file.getInt("menu.buttons." + key + ".item.data");
            int amounts = file.getInt("menu.buttons." + key + ".item.amount");
            int slot = file.getInt("menu.buttons." + key + ".slot");
            String name = file.getString("menu.buttons." + key + ".item.name");
            List<String> lore = file.getStringList("menu.buttons." + key + ".item.lore");
            CustomButtonAction actions = CustomButtonAction.NONE;
            try {
                actions = CustomButtonAction.valueOf(file.getString("menu.buttons." + key + ".action"));
            } catch (Exception e2) {
                OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe action for '&b" + key + "&c' is invalid.");
            }
            ItemTypes itemTypes = ItemTypes.NONE;
            try {
                itemTypes = ItemTypes.valueOf(file.getString("menu.buttons." + key + ".settings.sorted.type"));
            } catch (Exception e2) {
                OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe item-type for '&b" + key + "&c' is invalid.");
            }

            int amount = file.getInt("menu.buttons." + key + ".amount");
            int price = file.getInt("menu.buttons." + key + ".price");
            List<String> commands = file.getStringList("menu.buttons." + key + ".commands");
            String subMenu = file.getString("menu.buttons." + key + ".settings.category.name");
            ItemStack itemStack = ItemMaker.of(material).setData((short) data).setAmount(amounts).setDisplayName(name).setLore(lore).build();

            CustomButton button = new CustomButton();
            button.setButton(itemStack);
            button.setSlot(slot);
            button.setActions(actions);
            button.setSubMenu(subMenu);
            button.setPrice(price);
            button.setCommands(commands);
            button.setLore(lore);
            button.setItemType(itemTypes);
            button.setAmount(amount);
            button.setName(name);

            this.customButtonStorage.add(button);
        }
        OxygenBukkit.getInstance().debug_message("&cSuccessfully loaded &b" + this.customButtonStorage.size() + " Custom Buttons&c!");

    }
    public void loadCategories() {
        this.customCategoriesStorage.clear();

        ConfigurationSection section = OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getConfigurationSection("categories");

        section.getKeys(false).forEach(key -> {
            List<CustomButton> customButtons = new ArrayList<>();
            String title = section.getString(key + ".title");
            int rows = section.getInt(key + ".rows");
            boolean fill_toggle = section.getBoolean(key + ".fill.enabled");
            int fill_data = section.getInt(key + ".fill.data");
            String fill_style = section.getString(key + ".fill.style");
            List<String> fill_corners = section.getStringList(key + ".fill.slots");
            boolean fill_glow = section.getBoolean(key + ".fill.glow");
            int discount = section.getInt(key + ".discount");

            boolean back_toggle = section.getBoolean(key + ".back.enabled");
            String back_material = section.getString(key + ".back.item.material");
            int back_data = section.getInt(key + ".back.item.data");
            String back_name = section.getString(key + ".back.item.name");
            List<String> back_lore = section.getStringList(key + ".back.item.lore");
            int back_slot = section.getInt(key + ".back.slot");

            this.customCategoriesStorage.add(new CustomCategory(title, key, customButtons, rows, fill_toggle, fill_data, fill_style, fill_corners, fill_glow, discount, back_toggle, back_material, back_data, back_name, back_lore, back_slot));
        });

        OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cSuccessfully loaded &b" + this.customCategoriesStorage.size() + " Custom Categories&c!");
    }
    public void loadCategoriesButtons() {

        for (CustomCategory category : this.customCategoriesStorage) {
            ConfigurationSection section = OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getConfigurationSection("categories." + category.getName() + ".buttons");

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
                List<String> lore = section.getStringList(key + ".item.lore");
                int slot = section.getInt(key + ".slot");
                ItemTypes itemTypes = ItemTypes.NONE;
                try {
                    itemTypes = ItemTypes.valueOf(section.getString(key + ".settings.sorted.type"));
                } catch (Exception e2) {
                    OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe item-type for '&b" + key + "&c' is invalid.");
                }
                CustomButtonAction customButtonAction = CustomButtonAction.NONE;
                try {
                    customButtonAction = CustomButtonAction.valueOf(section.getString(key + ".action"));
                } catch (Exception e2) {
                    OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe action of '&b" + category.getName() + "-" + key + "&c' is invalid.");
                }

                int price = section.getInt(key + ".price");
                String subMenu = section.getString(key + ".settings.category.name");
                List<String> commands = section.getStringList(key + ".commands");
                int amount = section.getInt(key + ".amount");

                ItemStack itemStack = ItemMaker.of(material)
                        .setData((short) data)
                        .setAmount(amounts)
                        .setDisplayName(name)
                        .setLore(lore)
                        .build();

                CustomButton button = new CustomButton();
                button.setButton(itemStack);
                button.setSlot(slot);
                button.setActions(customButtonAction);
                button.setPrice(price);
                button.setSubMenu(subMenu);
                button.setCommands(commands);
                button.setLore(lore);
                button.setItemType(itemTypes);
                button.setAmount(amount);
                button.setName(name);

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
    public Optional<Sale> getActiveSaleFor(ItemTypes type) {
        return getActiveSales().values().stream().filter(activeSale -> activeSale.getApplyTypes() == type)
                .findFirst();
    }

    public void storeCurrencyLog(CurrencyLogging currencyLogging) {
        OxygenBukkit.getInstance().getMongoModule().getStoreCollection().insertOne(currencyLogging.toDocument());
    }

    public String getPricePlaceholderFormat(CustomCategory customCategory, CustomButton customButton) {
        int discount = this.getGlobalDiscount() > 0 ? this.getGlobalDiscount() : customCategory.getDiscount();

        if (discount > 0) {
            return (OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("placeholders.percent-sale-format").replace("%old_price%", String.valueOf(customButton.getPrice())).replace("%new_price%", String.valueOf((customButton.getPrice() * (100 - discount) / 100))).replace("%discount%", String.valueOf(discount)));
        } else if (this.getActiveSaleFor(customButton.getItemType()).isPresent()) {
            Sale sale = this.getActiveSaleFor(customButton.getItemType()).get();

            if (sale.getSaleType() == SaleTypes.ITEMS) {
                return (OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("placeholders.item-sale-format").replace("%price%", String.valueOf(customButton.getPrice())).replace("%amount%", String.valueOf(sale.getAmount())));
            } else if (sale.getSaleType() == SaleTypes.RANK) {
                return (OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("placeholders.rank-sale-format").replace("%price%", String.valueOf(customButton.getPrice())));
            }
        } else {
            return (OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getString("placeholders.default-price-format").replace("%price%", String.valueOf(customButton.getPrice())));
        }
        return "Unknown";
    }
}
