package dev.hely.core.bukkit.profile.tfa.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.tfa.utils
//   Date: Thursday, August 10, 2023 - 6:26 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;

@AllArgsConstructor
public class TFAQRGenerator extends MapRenderer {

    private Image image;

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        canvas.drawImage(0, 0, this.image);
    }
}

