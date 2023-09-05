package dev.hely.core.bukkit.coins.coupons;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.coupons
//   Date: Monday, September 04, 2023 - 8:59 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter
@Setter
public class Coupon {
    private int id;
    private UUID playerUuid;
    private String descMessage;
    private double value;
    private long time;
    private boolean redeemed;

    public Coupon(Document document) {
        this.id = document.getInteger("id");
        this.playerUuid = UUID.fromString(document.getString("playerUuid"));
        this.descMessage = document.getString("descMessage");
        this.value = document.getDouble("value");
        this.time = document.getLong("time");
        this.redeemed = document.getBoolean("redeemed");
    }

    public Coupon(UUID playerUuid, String descMessage, double value) {
        this.id = (OxygenBukkit.getInstance().getCouponManager().getCoupons().size());
        this.playerUuid = playerUuid;
        this.descMessage = descMessage;
        this.value = value;
        this.time = System.currentTimeMillis();
        this.redeemed = false;
    }

}
