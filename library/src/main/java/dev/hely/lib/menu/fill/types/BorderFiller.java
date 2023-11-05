package dev.hely.lib.menu.fill.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.lib.menu.fill.types
//   Date: Saturday, September 16, 2023 - 1:05 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.fill.MenuFiller;
import dev.hely.lib.menu.implement.PaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class BorderFiller implements MenuFiller {

    @Override
    public void fill(Menu menu, Player player, Map<Integer, Button> buttons, int size, Inventory inventory) {
        int startIndex = menu instanceof PaginatedMenu ? 8 : 0;
        for (int i = startIndex; i < size; i++) {
            if (i < startIndex + 9) {
                buttons.putIfAbsent(i, Button.getPlaceholder());
                buttons.putIfAbsent(i + (size - 9), Button.getPlaceholder());
            }

            if (i % 9 == 0) {
                buttons.putIfAbsent(i, Button.getPlaceholder());
                buttons.putIfAbsent(i + 8, Button.getPlaceholder());
            }
        }
    }

}
