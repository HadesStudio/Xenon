package dev.hely.core.bungee.server.data.server.data;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.data.server.data
//   Date: Monday, August 14, 2023 - 11:27 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusData {

    ONLINE("Online", "Online"),
    OFFLINE("Offline", "Offline"),
    WHITELISTED("Whitelisted", "Whitelisted");

    @Getter private String name;
    @Getter private String display;
}

