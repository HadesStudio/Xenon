package dev.hely.core.bukkit.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSVersion {
    ItemStack getItemInHand(Player player);

    void setItemInHand(Player player, ItemStack itemStack);
}
