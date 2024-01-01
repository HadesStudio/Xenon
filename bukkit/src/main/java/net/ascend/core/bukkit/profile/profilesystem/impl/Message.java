package net.ascend.core.bukkit.profile.profilesystem.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.impl
//   Date: Monday, July 10, 2023 - 12:50 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public class Message {
    @Getter private UUID target;
    @Getter private UUID sender;
    @Getter private boolean read;
    @Getter private String comment;
}
