package dev.hely.core.bukkit.coins.item;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.item
//   Date: Monday, August 07, 2023 - 6:28 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.coins.item.type.ItemTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
@Setter
@AllArgsConstructor
public class ShopItem {
    private String name;
    private ItemTypes type;
    private List<String> description;
    private ItemStack displayItem;
    private int price;
    private int slot;
    private int amount;
    private Predicate<Player> canBuy;
    private Consumer<Player> onBuy;
    private Consumer<Player> onClickInMenu;

    public ShopItem(String name, ItemTypes type, List<String> description, ItemStack displayItem, int price, int slot, Consumer<Player> onBuy, Consumer<Player> onClickInMenu) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.displayItem = displayItem;
        this.price = price;
        this.slot = slot;
        this.onBuy = onBuy;
        this.onClickInMenu = onClickInMenu;
        this.amount = 1;
    }

    public ShopItem(String name, ItemTypes type, List<String> description, ItemStack displayItem, int price, int slot, Predicate<Player> canBuy, Consumer<Player> onBuy, Consumer<Player> onClickInMenu) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.displayItem = displayItem;
        this.price = price;
        this.slot = slot;
        this.onBuy = onBuy;
        this.onClickInMenu = onClickInMenu;
        this.amount = 1;
        this.canBuy = canBuy;
    }
}
