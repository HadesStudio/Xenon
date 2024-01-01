package net.ascend.lib.menu.fill;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.lib.menu.fill
//   Date: Saturday, September 16, 2023 - 1:04 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.lib.menu.Menu;
import net.ascend.lib.menu.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public interface MenuFiller {

    void fill(Menu menu, Player player, Map<Integer, Button> buttons, int size, Inventory inventory);

}
