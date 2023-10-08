package dev.hely.core.bungee.maintenance;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.maintenance
//   Date: Sunday, August 13, 2023 - 12:50 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.modules.ModuleLoader;

public class MaintenanceManager implements ModuleLoader {
    public boolean enabled;

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Maintenance";
    }
}
