package dev.hely.core.bukkit.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.utils
//   Date: Saturday, August 26, 2023 - 4:49 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import dev.hely.lib.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;

public class BukkitUtils {
    private static final ImmutableMap<ChatColor, DyeColor> CHAT_DYE_COLOUR_MAP;
    private static final String STRAIGHT_LINE_TEMPLATE;
    public static String STRAIGHT_LINE_DEFAULT;

    static {
        STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH + Strings.repeat("-", 256);
        STRAIGHT_LINE_DEFAULT = BukkitUtils.STRAIGHT_LINE_TEMPLATE.substring(0, 55);
        CHAT_DYE_COLOUR_MAP = ImmutableMap.<ChatColor, DyeColor>builder().put(ChatColor.AQUA, DyeColor.LIGHT_BLUE).put(ChatColor.BLACK, DyeColor.BLACK).put(ChatColor.BLUE, DyeColor.LIGHT_BLUE).put(ChatColor.DARK_AQUA, DyeColor.CYAN).put(ChatColor.DARK_BLUE, DyeColor.BLUE).put(ChatColor.DARK_GRAY, DyeColor.GRAY).put(ChatColor.DARK_GREEN, DyeColor.GREEN).put(ChatColor.DARK_PURPLE, DyeColor.PURPLE).put(ChatColor.DARK_RED, DyeColor.RED).put(ChatColor.GOLD, DyeColor.ORANGE).put(ChatColor.GRAY, DyeColor.SILVER).put(ChatColor.GREEN, DyeColor.LIME).put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA).put(ChatColor.RED, DyeColor.RED).put(ChatColor.WHITE, DyeColor.WHITE).put(ChatColor.YELLOW, DyeColor.YELLOW).build();
    }

    public static Player getDamageSource(Entity damager) {

        Player playerDamager = null;

        if (damager instanceof Player) {
            playerDamager = (Player)damager;
        } else if (damager instanceof Projectile) {

            final Projectile projectile = (Projectile)damager;

            if (projectile.getShooter() instanceof Player) {
                playerDamager = (Player)projectile.getShooter();
            }
        }

        return playerDamager;
    }

    public static Player getFinalAttacker(EntityDamageEvent ede, boolean ignoreSelf) {
        Player attacker = null;
        if (ede instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ede;
            Entity damager = event.getDamager();
            if (event.getDamager() instanceof Player) {
                attacker = (Player) damager;
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) damager;
                ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof Player) {
                    attacker = (Player) shooter;
                }
            }

            if (attacker != null && ignoreSelf && event.getEntity().equals(attacker)) {
                attacker = null;
            }
        }

        return attacker;
    }

    public static DyeColor toDyeColor(ChatColor colour) {
        return BukkitUtils.CHAT_DYE_COLOUR_MAP.get(colour);
    }

    public static ChatColor getChatColorByCode(String colorCode) {
        return ChatColor.getByChar(colorCode.charAt(1));
    }

    public static Player getAttacker(EntityDamageEvent ede, boolean ignoreSelf) {
        Player attacker = null;
        if (ede instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ede;
            Entity damager = event.getDamager();
            if (event.getDamager() instanceof Player) {
                attacker = (Player) damager;
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) damager;
                ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof Player) {
                    attacker = (Player) shooter;
                }
            }
            if (attacker != null && ignoreSelf && event.getEntity().equals(attacker)) {
                attacker = null;
            }
        }
        return attacker;
    }

    public static Location getHighestLocation(Location origin) {
        return getHighestLocation(origin, null);
    }

    public static Location getHighestLocation(Location origin, Location def) {
        Preconditions.checkNotNull((Object) origin, "The location cannot be null");
        Location cloned = origin.clone();
        World world = cloned.getWorld();
        int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            Block block = world.getBlockAt(x, --y, z);
            if (!block.isEmpty()) {
                Location next = block.getLocation();
                next.setPitch(origin.getPitch());
                next.setYaw(origin.getYaw());
                return next;
            }
        }
        return def;
    }

    public static String getPotionEffectName(PotionEffect effect) {
        return effect.getAmplifier() == 0 ? getPotionEffectName(effect.getType()) :
                getPotionEffectName(effect.getType()) + " " + getPotionEffectLevel(effect);
    }

    public static String getPotionEffectName(PotionEffectType type) {
        switch (type.getName()) {
            case "ABSORPTION":
                return "Absorption";
            case "BLINDNESS":
                return "Blindness";
            case "CONFUSION":
                return "Confusion";
            case "DAMAGE_RESISTANCE":
                return "Resistance";
            case "FAST_DIGGING":
                return "Haste";
            case "FIRE_RESISTANCE":
                return "Fire Resistance";
            case "HARM":
                return "Instant Damage";
            case "HEAL":
                return "Instant Health";
            case "HEALTH_BOOST":
                return "Health Boost";
            case "HUNGER":
                return "Hunger";
            case "INCREASE_DAMAGE":
                return "Strength";
            case "INVISIBILITY":
                return "Invisibility";
            case "JUMP":
                return "Jump";
            case "NIGHT_VISION":
                return "Night Vision";
            case "POISON":
                return "Poison";
            case "REGENERATION":
                return "Regeneration";
            case "SATURATION":
                return "Saturation";
            case "SLOW":
                return "Slowness";
            case "SLOW_DIGGING":
                return "Slow Digging";
            case "SPEED":
                return "Speed";
            case "WATER_BREATHING":
                return "Water Breathing";
            case "WEAKNESS":
                return "Weakness";
            case "WITHER":
                return "Wither";
            default:
                return "Unknown potion effect";
        }
    }

    private static String getPotionEffectLevel(PotionEffect effect) {
        switch (effect.getAmplifier()) {
            case 0:
                return "";
            case 1:
                return "II";
            case 2:
                return "III";
            case 3:
                return "IV";
            case 4:
                return "V";
            case 5:
                return "VI";
            case 6:
                return "VII";
            case 7:
                return "VIII";
            case 8:
                return "IX";
            case 9:
                return "X";
            default:
                return "Unknown potion effect level";
        }
    }

    public static String getEnchantName(String enchant) {
        switch (enchant) {
            case "PROTECTION":
                return "PROTECTION_ENVIRONMENTAL";
            case "FIRE_PROTECTION":
                return "PROTECTION_FIRE";
            case "FEATHER_FALLING":
                return "PROTECTION_FALL";
            case "BLAST_PROTECTION":
                return "PROTECTION_EXPLOSIONS";
            case "PROJECTILE_PROTECTION":
                return "PROTECTION_PROJECTILE";
            case "RESPIRATION":
                return "OXYGEN";
            case "AQUA_AFFINITY":
                return "WATER_WORKER";
            case "THORNS":
                return "THORNS";
            case "SHARPNESS":
                return "DAMAGE_ALL";
            case "SMITE":
                return "DAMAGE_UNDEAD";
            case "BANE_OF_ARTHROPODS":
                return "DAMAGE_ARTHROPODS";
            case "KNOCKBACK":
                return "KNOCKBACK";
            case "FIRE_ASPECT":
                return "FIRE_ASPECT";
            case "LOOTING":
                return "LOOT_BONUS_MOBS";
            case "EFFICIENCY":
                return "DIG_SPEED";
            case "SILK_TOUCH":
                return "SILK_TOUCH";
            case "UNBREAKING":
                return "DURABILITY";
            case "DURABILITY":
                return "DURABILITY";
            case "FORTUNE":
                return "LOOT_BONUS_BLOCKS";
            case "POWER":
                return "ARROW_DAMAGE";
            case "PUNCH":
                return "ARROW_KNOCKBACK";
            case "FLAME":
                return "ARROW_FIRE";
            case "INFINITY":
                return "ARROW_INFINITE";
            case "LUCK":
                return "LUCK";
            case "LURE":
                return "LURE";
            default:
                return "Unknown enchantment";
        }
    }

    public static String getEnchantName(Enchantment enchantment) {
        switch (enchantment.getName()) {
            case "PROTECTION_ENVIRONMENTAL":
                return "Protection";
            case "PROTECTION_FIRE":
                return "Fire Protection";
            case "PROTECTION_FALL":
                return "Feather Falling";
            case "PROTECTION_EXPLOSIONS":
                return "Blast Protection";
            case "PROTECTION_PROJECTILE":
                return "Projectile Protection";
            case "OXYGEN":
                return "Respiration";
            case "WATER_WORKER":
                return "Aqua Affinity";
            case "THORNS":
                return "Thorns";
            case "DAMAGE_ALL":
                return "Sharpness";
            case "DAMAGE_UNDEAD":
                return "Smite";
            case "DAMAGE_ARTHROPODS":
                return "Bane of Arthropods";
            case "KNOCKBACK":
                return "Knockback";
            case "FIRE_ASPECT":
                return "Fire Aspect";
            case "LOOT_BONUS_MOBS":
                return "Looting";
            case "DIG_SPEED":
                return "Efficiency";
            case "SILK_TOUCH":
                return "Silk Touch";
            case "DURABILITY":
                return "Unbreaking";
            case "LOOT_BONUS_BLOCKS":
                return "Fortune";
            case "ARROW_DAMAGE":
                return "Power";
            case "ARROW_KNOCKBACK":
                return "Punch";
            case "ARROW_FIRE":
                return "Flame";
            case "ARROW_INFINITE":
                return "Infinity";
            case "LUCK":
                return "Luck";
            case "LURE":
                return "Lure";
            default:
                return "Unknown enchantment";
        }
    }

    public static String joinArray(String[] array, String delimiter, int start) {
        if (array.length < start) return "";

        return String.join(CC.translate(delimiter), Arrays.copyOfRange(array, start - 1, array.length));
    }
}
