package dev.hely.core.managers.database.redis;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.managers.database.redis
//   Date: Tuesday, July 04, 2023 - 6:35 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;

import java.util.Arrays;

public enum ChannelType {

    BUNGEE("bungee"), VELOCITY("velocity");

    @Getter
    private final String name;

    ChannelType(String name) {
        this.name = name;
    }

    public static ChannelType fromName(String name) {
        return Arrays.stream(ChannelType.values()).filter(in -> in.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
