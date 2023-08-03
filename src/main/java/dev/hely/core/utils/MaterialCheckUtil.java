package dev.hely.core.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.utils
//   Date: Saturday, July 15, 2023 - 3:02 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class MaterialCheckUtil {
    public static Material checkMaterial(String material, String describer, Material default_material) {
        if (Material.getMaterial(material) == null) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material for '" + describer + "' is invalid. We have setup a default value for the time being."));
            return default_material;
        } else {
            return Material.getMaterial(material);
        }
    }
}
