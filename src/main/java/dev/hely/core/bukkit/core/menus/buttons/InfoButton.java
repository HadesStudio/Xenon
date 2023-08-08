package dev.hely.core.bukkit.core.menus.buttons;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.core.menus.buttons
//   Date: Tuesday, August 08, 2023 - 1:16 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class InfoButton extends Button {
    @Override
    public ItemStack getItemStack(Player player) {
        ItemMaker itemMaker = ItemMaker.of(Material.BOOK);
        itemMaker.setDisplayName("&6&lInformation");
        itemMaker.setData((short) 0);
        itemMaker.addFakeGlow();

        itemMaker.addLore("");
        itemMaker.addLore("&7Thank you for using &6Oxygen Rank Core&7!");
        itemMaker.addLore("");
        itemMaker.addLore("&7If you encounter any bugs, or have any");
        itemMaker.addLore("&7suggestions please feel free to let us");
        itemMaker.addLore("&7know in our &dDiscord Server&7!");
        itemMaker.addLore("&7");
        itemMaker.addLore("&dDiscord: &6discord.gg/helydev");
        return itemMaker.build();
    }


    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        player.sendMessage(CC.translate("&dDiscord Server: &6discord.gg/helydev"));
    }

    @Override
    public boolean toUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}
