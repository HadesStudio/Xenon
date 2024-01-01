package net.ascend.core.bukkit.staff.staffmode.data;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.staff.staffmode.data
//   Date: Saturday, August 26, 2023 - 3:45 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class StaffPlayerData {
    private ItemStack[] contents;
    private ItemStack[] armor;
    private GameMode gameMode;
}
