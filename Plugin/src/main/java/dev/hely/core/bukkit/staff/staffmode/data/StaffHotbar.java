package dev.hely.core.bukkit.staff.staffmode.data;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.staff.staffmode.data
//   Date: Saturday, August 26, 2023 - 3:44 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class StaffHotbar {
    public final ItemStack itemStack;
    public final int slot;
    public final boolean enabled;
}

