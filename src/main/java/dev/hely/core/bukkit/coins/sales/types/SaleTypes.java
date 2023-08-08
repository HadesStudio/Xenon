package dev.hely.core.bukkit.coins.sales.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.sales.types
//   Date: Monday, August 07, 2023 - 5:32 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

public enum SaleTypes {
    RANK,
    ITEMS,
    PERCENT;

    public static SaleTypes getByName(String name) {
        for (SaleTypes type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
