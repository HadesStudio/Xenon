package dev.hely.core.bukkit.nms.implement;

import dev.hely.core.bukkit.nms.NMSVersion;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class v1_15NMS implements NMSVersion {
    @Override
    public ItemStack getItemInHand(Player player) {
        return player.getItemInHand();
    }

    @Override
    public void setItemInHand(Player player, ItemStack itemStack) {
        player.setItemInHand(itemStack);
    }
}
