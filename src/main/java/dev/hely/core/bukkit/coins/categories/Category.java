package dev.hely.core.bukkit.coins.categories;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.categories
//   Date: Monday, August 07, 2023 - 6:33 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.collect.Lists;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.item.ShopItem;
import dev.hely.core.bukkit.coins.logging.CurrencyLogging;
import dev.hely.core.bukkit.coins.logging.types.CurrencyActions;
import dev.hely.core.bukkit.coins.sales.Sale;
import dev.hely.core.bukkit.coins.sales.types.SaleTypes;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.lib.CC;
import dev.hely.lib.menu.Menu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public abstract class Category {
    private boolean enabled = true;
    private int discount = 0;
    private List<ShopItem> items = Lists.newArrayList();
    private Menu menu;

    public Category() {
        loadItems();
    }

    public void loadItems() {
    }

    public abstract String getName();

    public abstract String getDisplayName();

    public abstract List<String> getDescription();

    public abstract ItemStack getDisplayIcon();

    public abstract int getMenuSlot();

    public List<Category> getSubCategories() {
        return null;
    }

    public void buyItem(ShopItem item, Player player) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        int currency = profile.getCurrency();
        int amount = item.getAmount();
        int discount = OxygenBukkit.getInstance().getStoreManager().getGlobalDiscount() > 0 ? OxygenBukkit.getInstance().getStoreManager().getGlobalDiscount() : this.discount;

        int totalPrice = item.getPrice() * (100 - discount) / 100;

        CurrencyLogging log = new CurrencyLogging(player.getUniqueId(),
                currency,
                totalPrice,
                System.currentTimeMillis(),
                CurrencyActions.PURCHASED,
                item.getName());

        profile.setCurrency(currency - totalPrice);
        profile.save();

        boolean hasAppliedSale = false;

        for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.purchase.broadcast")) {
            Bukkit.broadcastMessage(CC.translate(message)
                    .replace("%price%", String.valueOf(totalPrice))
                    .replace("%item%", item.getName())
                    .replace("%player%", player.getName())
            );
        }

        for (Sale activeSale : OxygenBukkit.getInstance().getStoreManager().getActiveSales().values()) {

            if (!activeSale.isActive()){
                continue;
            }

            if (activeSale.getApplyTypes() == item.getType()) {
                activeSale.broadcast(player, item.getName(), amount);
                if (activeSale.getSaleType() == SaleTypes.ITEMS) { // BUY ONE GET XXX SALE
                    for (int i = 0; i < activeSale.getAmount(); i++) {
                        item.getOnBuy().accept(player);
                    }
                    hasAppliedSale = true;
                }else if (activeSale.getSaleType() == SaleTypes.RANK) {
                    for (String message : OxygenBukkit.getInstance().getCoinShopConfig().getConfiguration().getStringList("messages.sales.rank-sale.type-name")) {
                        Bukkit.broadcastMessage(CC.translate(message));
                    }
                    hasAppliedSale = true;
                }
            }
        }

        if (!hasAppliedSale) {
            item.getOnBuy().accept(player);
        }

        OxygenBukkit.getInstance().getStoreManager().storeCurrencyLog(log);
    }

    public boolean hasDiscount() {
        return discount > 0;
    }
}

