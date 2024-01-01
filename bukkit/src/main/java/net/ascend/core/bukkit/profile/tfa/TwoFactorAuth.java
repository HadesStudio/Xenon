package net.ascend.core.bukkit.profile.tfa;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.tfa
//   Date: Thursday, August 10, 2023 - 5:06 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import net.ascend.core.bukkit.XenonBukkit;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;

@Getter
@Setter
public class TwoFactorAuth {
    private final String secretKey;
    private boolean authenticated;
    private String latestIp;
    private int tries;
    private ItemStack restoreItem;
    private boolean firstAuth;
    private final String IMAGE_URL_FORMAT = "https://www.google.com/chart?chs=130x130&chld=M%%7C0&cht=qr&chl=%s";

    public TwoFactorAuth(final Document document) {
        this.authenticated = false;
        this.firstAuth = false;
        this.secretKey = document.getString("secretKey");
        this.authenticated = document.getBoolean("authenticated");
        this.latestIp = document.getString("latestIp");
        this.tries = document.getInteger("tries");
        this.firstAuth = document.getBoolean("firstAuth", true);
    }

    public boolean isValid(int code) {
        return XenonBukkit.getInstance().getGoogleAuthenticator().authorize(this.secretKey, code);
    }

    public boolean needAuthentication(Player player) {
        return this.latestIp == null || !this.authenticated || !this.latestIp.equals(player.getAddress().getAddress().getHostAddress());
    }

    public Document serialize() {
        return new Document("secretKey", this.secretKey).append("authenticated", this.authenticated).append("latestIp", this.latestIp).append("tries", this.tries).append("firstAuth", this.firstAuth);
    }

    public void sendQR(final Player player) {
        final BufferedImage image = this.generateImage(player, this.secretKey);
        if (image != null) {
            final MapView mapView = Bukkit.getServer().createMap(player.getWorld());
            mapView.getRenderers().forEach(mapView::removeRenderer);
            mapView.addRenderer(new QRCodeRenderer(player.getUniqueId(), image));
            final ItemStack mapItem = new ItemStack(Material.MAP, 1, (short)mapView.getId());
            final ItemMeta mapMeta = mapItem.getItemMeta();
            mapMeta.setLore(Collections.singletonList("QR Code Map"));
            mapItem.setItemMeta(mapMeta);
            player.sendMap(mapView);
            this.restoreItem = player.getInventory().getItem(0);
            player.getInventory().setItem(0, mapItem);
            player.updateInventory();
        }
    }

    public void restoreItem(final Player player) {
        if (this.restoreItem != null) {
            player.getInventory().setItem(0, this.restoreItem);
            player.updateInventory();
            this.restoreItem = null;
        }
    }

    private BufferedImage generateImage(final Player player, final String secret) {
        final Escaper urlEscaper = UrlEscapers.urlFragmentEscaper();
        final String url = "otpauth://totp/" + urlEscaper.escape(player.getName()) + "?secret=" + secret + "&issuer=Xenon";
        final String imageUrl = String.format("https://www.google.com/chart?chs=130x130&chld=M%%7C0&cht=qr&chl=%s", URLEncoder.encode(url));
        try {
            return ImageIO.read(new URL(imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TwoFactorAuth(final String secretKey) {
        this.authenticated = false;
        this.firstAuth = false;
        this.secretKey = secretKey;
    }
}
