package dev.hely.core.bungee.utils.module;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.utils.module
//   Date: Sunday, August 13, 2023 - 12:50 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

public interface ModuleLoader {
    void enable();

    void disable();

    void reload();

    boolean isEnabled();

    String getName();
}
