package net.ascend.core.bukkit.profile.tfa.data;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.tfa.data
//   Date: Thursday, August 10, 2023 - 5:53 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class AuthData {
    private ItemStack[] invItems;
    private ItemStack[] armor;
    private GameMode gameMode;
    private Location location; // Incase the player moved somehow??
}
