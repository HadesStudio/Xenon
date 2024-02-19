package net.ascend.core.bukkit.lib.menu.fill.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.lib.menu.fill.types
//   Date: Saturday, September 16, 2023 - 1:07 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.fill.MenuFiller;
import net.ascend.core.bukkit.lib.menu.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class FillFiller implements MenuFiller {
    private final Button button = Button.getPlaceholder();

    @Override
    public void fill(Menu menu, Player player, Map<Integer, Button> buttons, int size, Inventory inventory) {
        for (int index = 0; index < size; index++) {
            if (buttons.get(index) == null) {
                buttons.put(index, button);
                inventory.setItem(index, button.getItemStack(player));
            }
        }
    }
}
