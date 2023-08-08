package dev.hely.core.bukkit.coins.sales;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.sales
//   Date: Monday, August 07, 2023 - 5:31 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.item.type.ItemTypes;
import dev.hely.core.bukkit.coins.sales.types.SaleTypes;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@Getter
@AllArgsConstructor
public class Sale {
    private final int amount;
    private final ItemTypes applyTypes;
    private long endAt;
    private SaleTypes saleType;

    public boolean isActive() {
        return System.currentTimeMillis() < endAt;
    }

    public void broadcast(Player player, String item, int amount) {
        if (saleType == SaleTypes.ITEMS) {
            for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.sales.item-sale")) {
                Bukkit.broadcastMessage(CC.translate(message)
                        .replace("%player_name%", player.getName())
                        .replace("%amount%", String.valueOf(amount * this.amount))
                        .replace("%item%", item)
                );
            }

            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
            //        .replace("%player%", player.getName())
            //        .replace("%amount%", String.valueOf((amount * this.amount)))
            //);
        } else if (saleType == SaleTypes.RANK) {
            for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.sales.rank-sale.broadcast")) {
                Bukkit.broadcastMessage(CC.translate(message)
                        .replace("%player_name%", player.getName())
                        .replace("%item%", item)
                );
            }

            player.setMetadata("rank_sale_typing_name", new FixedMetadataValue(OxygenBukkit.getInstance(), true));
        } else {
            for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.sales.rank-sale.broadcast")) {
                Bukkit.broadcastMessage(CC.translate(message)
                        .replace("%player_name%", player.getName())
                        .replace("%amount%", String.valueOf(this.amount))
                        .replace("%item%", item)
                );
            }
        }
    }
}
