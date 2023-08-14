package dev.hely.core.bungee.server.data.proxy.data;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.data.proxy.data
//   Date: Monday, August 14, 2023 - 11:19 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ProxyServerStatus {
    ONLINE("Online"),
    OFFLINE("Offline"),
    STARTING("Starting Up");

    @Getter
    private String name;
}

