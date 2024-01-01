package net.ascend.core.bukkit.profile.tfa.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.tfa.utils
//   Date: Thursday, August 10, 2023 - 6:19 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TFAUtil {
    public static String getQrImageURL(String message, String name) {
        return "https://chart.googleapis.com/chart?chs=128x128&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=otpauth://totp/" + name + "?secret=" + message;
    }

    public static int getHotbarSlotOfItem(ItemStack item, Player player) {
        if (item == null) return -1;
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).equals(item)) {
                return i;
            }
        }
        return 0;
    }

    public static void removeQrMapFromInventory(Player player) {
        for (int i = 0; i < 9; i++) {
            ItemStack item = player.getInventory().getItem(i);

            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).contains("QR")) {
                player.getInventory().remove(item);
            }
        }
    }

    public static boolean checkCode(String message, int code) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        return googleAuthenticator.authorize(message, code);
    }
}
