package dev.hely.core.bukkit.coins.coupons;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.coupons
//   Date: Monday, September 04, 2023 - 9:01 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class CouponManager implements ModuleLoader {
    private List<Coupon> coupons = new ArrayList<>();
    public boolean enabled;

    @Override
    public void enable() {
        enabled = true;
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getCouponsCollection().find()) {
            coupons.add(new Coupon(document));
        }

        // we need make sure this updates atleast every so often ;( puta
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(OxygenBukkit.getInstance(), this::save, (20*60)*15, (20*60)*15);
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return "Coupon";
    }

    public void save() {
        for (Coupon coupon : coupons) {
            Document document = new Document();

            document.put("id", coupon.getId());
            document.put("playerUuid", coupon.getPlayerUuid().toString());
            document.put("descMessage", coupon.getDescMessage());
            document.put("value", coupon.getValue());
            document.put("time", coupon.getTime());
            document.put("redeemed", coupon.isRedeemed());

            OxygenBukkit.getInstance().getMongoModule().getCouponsCollection().replaceOne(Filters.eq("id", coupon.getId()), document, new ReplaceOptions().upsert(true));
        }
    }


    public List<Coupon> getCouponsForPlayer(UUID uuid) {
        return coupons.stream().filter(coupon -> coupon.getPlayerUuid().toString().equalsIgnoreCase(uuid.toString())).collect(Collectors.toList());
    }

    public List<Coupon> getCouponById(int id) {
        return coupons.stream().filter(coupon -> coupon.getId() == id).collect(Collectors.toList());
    }
}
