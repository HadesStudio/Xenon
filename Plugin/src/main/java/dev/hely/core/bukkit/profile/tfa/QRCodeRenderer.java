package dev.hely.core.bukkit.profile.tfa;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.tfa
//   Date: Thursday, August 10, 2023 - 5:07 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class QRCodeRenderer extends MapRenderer {
    private UUID uuid;
    private BufferedImage image;

    public void render(MapView map, MapCanvas canvas, Player player) {
        if (player.getUniqueId().equals(this.uuid)) {
            canvas.drawImage(0, 0, this.image);
        }
    }

    public QRCodeRenderer(UUID uuid, BufferedImage image) {
        this.uuid = uuid;
        this.image = image;
    }
}

