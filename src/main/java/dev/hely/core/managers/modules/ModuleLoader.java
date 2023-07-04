package dev.hely.core.managers.modules;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.managers.modules
//   Date: Monday, July 03, 2023 - 2:25 PM
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
