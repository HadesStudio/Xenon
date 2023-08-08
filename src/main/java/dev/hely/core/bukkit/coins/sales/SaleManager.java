package dev.hely.core.bukkit.coins.sales;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.sales
//   Date: Monday, August 07, 2023 - 5:31 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.coins.sales.types.SaleTypes;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class SaleManager {
    @Getter private static final Map<SaleTypes, Sale> sales = new HashMap<>();

    public Sale geByType(SaleTypes type) {
        return sales.get(type);
    }
}
