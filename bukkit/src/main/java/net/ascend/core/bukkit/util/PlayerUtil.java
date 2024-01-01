package net.ascend.core.bukkit.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class PlayerUtil {

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
}
