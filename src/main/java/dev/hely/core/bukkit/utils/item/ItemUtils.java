package dev.hely.core.bukkit.utils.item;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.JavaUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ItemUtils {

    private static Map<String, String> ITEMS_BY_NAME;
    private static Map<String, String> ITEMS_BY_MATERIAL;


    public static void tryFit(Player p, ItemStack item) {
        PlayerInventory inv = p.getInventory();
        boolean canfit = false;
        for (int i = 0; i < inv.getSize(); ++i) {
            if (inv.getItem(i) == null || inv.getItem(i) != null && inv.getItem(i).getType() == Material.AIR) {
                canfit = true;
                inv.addItem(item);
                break;
            }
        }
        if (!canfit) {
            p.getWorld().dropItemNaturally(p.getLocation(), item);
        }
    }

    public static int getItemAmount(Player player, MaterialData data) {
        int amount = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != data.getItemType()) continue;
            if (data.getItemType().getMaxDurability() == 0 && item.getData().getData() != data.getData()) continue;

            amount += item.getAmount();
        }

        return amount;
    }

    public static void removeItem(Inventory inventory, MaterialData data, int amount) {
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];

            if (item == null || data.getItemType() != item.getType()) continue;
            if (data.getItemType().getMaxDurability() == 0 && item.getData().getData() != data.getData()) continue;

            if (amount >= item.getAmount()) {
                amount -= item.getAmount();
                inventory.setItem(i, null);
            } else {
                item.setAmount(item.getAmount() - amount);
                break;
            }
        }
    }


    public static ItemStack parseItem(String value) {
        String itemData = ITEMS_BY_NAME.get(value);

        if (itemData != null) {
            String[] itemArray = itemData.split(":");

            Material material = Material.getMaterial(Integer.parseInt(itemArray[0]));
            short durability = Short.parseShort(itemArray[1]);
            return new ItemStack(material, 1, durability);
        }

        Material material = Material.getMaterial(value);

        if (material != null) {
            return new ItemStack(material);
        }

        String type;
        String durabilityString;

        if (value.contains(":")) {
            String[] itemArray = value.split(":");
            if (itemArray.length < 2) return null;

            type = itemArray[0];
            durabilityString = itemArray[1];
        } else {
            type = value;
            durabilityString = "0";
        }

        if (!JavaUtils.isInteger(type)) {
            return null;
        }

        material = Material.getMaterial(Integer.parseInt(type));

        if (material == null || !JavaUtils.isInteger(durabilityString)) {
            return null;
        }

        short durability = Short.parseShort(durabilityString);
        return new ItemStack(material, 1, durability);
    }

    public static String getItemName(ItemStack item) {
        String itemName = ITEMS_BY_MATERIAL.get(item.getTypeId() + ":" + item.getDurability());
        return itemName != null ? itemName : ITEMS_BY_MATERIAL.get(item.getTypeId() + ":0");
    }

    public static String getMaterialName(Material material) {
        return ITEMS_BY_MATERIAL.get(material.getId() + ":0");
    }

    public static boolean hasEnchantment(ItemStack item, Enchantment enchantment) {
        return item != null && item.containsEnchantment(enchantment);
    }

    public static void setupItemStack() {
        ITEMS_BY_NAME = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        ITEMS_BY_MATERIAL = new HashMap<>();

        OxygenBukkit.getInstance().getItemsConfig().getConfig().getKeys(false).forEach(name -> ITEMS_BY_NAME.put(name, OxygenBukkit.getInstance().getItemsConfig().getConfig().getString(name)));

        ITEMS_BY_NAME.forEach((name, id) -> ITEMS_BY_MATERIAL.put(id, name));
    }

    public static EntityType getSpawnerType(short durability) {
        switch (durability) {
            case 50:
                return EntityType.CREEPER;
            case 51:
                return EntityType.SKELETON;
            case 52:
                return EntityType.SPIDER;
            case 53:
                return EntityType.GIANT;
            case 54:
                return EntityType.ZOMBIE;
            case 55:
                return EntityType.SLIME;
            case 56:
                return EntityType.GHAST;
            case 57:
                return EntityType.valueOf("PIG_ZOMBIE");
            case 58:
                return EntityType.ENDERMAN;
            case 59:
                return EntityType.CAVE_SPIDER;
            case 60:
                return EntityType.SILVERFISH;
            case 61:
                return EntityType.BLAZE;
            case 62:
                return EntityType.MAGMA_CUBE;
            case 63:
                return EntityType.ENDER_DRAGON;
            case 64:
                return EntityType.WITHER;
            case 65:
                return EntityType.BAT;
            case 66:
                return EntityType.WITCH;
            case 91:
                return EntityType.SHEEP;
            case 92:
                return EntityType.COW;
            case 93:
                return EntityType.CHICKEN;
            case 94:
                return EntityType.SQUID;
            case 95:
                return EntityType.WOLF;
            case 96:
                return EntityType.MUSHROOM_COW;
            case 97:
                return EntityType.SNOWMAN;
            case 98:
                return EntityType.OCELOT;
            case 99:
                return EntityType.IRON_GOLEM;
            case 100:
                return EntityType.HORSE;
            default:
                return EntityType.PIG;
        }
    }

    public static int getSpawnerDurability(EntityType type) {
        switch (type) {
            case CREEPER:
                return 50;
            case SKELETON:
                return 51;
            case SPIDER:
                return 52;
            case GIANT:
                return 53;
            case ZOMBIE:
                return 54;
            case SLIME:
                return 55;
            case GHAST:
                return 56;
            case PIG_ZOMBIE:
                return 57;
            case ENDERMAN:
                return 58;
            case CAVE_SPIDER:
                return 59;
            case SILVERFISH:
                return 60;
            case BLAZE:
                return 61;
            case MAGMA_CUBE:
                return 62;
            case ENDER_DRAGON:
                return 63;
            case WITHER:
                return 64;
            case BAT:
                return 65;
            case WITCH:
                return 66;
            case SHEEP:
                return 91;
            case COW:
                return 92;
            case CHICKEN:
                return 93;
            case SQUID:
                return 94;
            case WOLF:
                return 95;
            case MUSHROOM_COW:
                return 96;
            case SNOWMAN:
                return 97;
            case OCELOT:
                return 98;
            case IRON_GOLEM:
                return 99;
            case HORSE:
                return 100;
            default:
                return 90;
        }
    }
}
