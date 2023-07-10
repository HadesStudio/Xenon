package dev.hely.core.profile.profilesystem.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.impl
//   Date: Monday, July 10, 2023 - 12:51 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Comment {
    @Getter private UUID target;
    @Getter private UUID player;
    @Getter private String comment;
    @Getter private List<UUID> likes = new ArrayList<>();
}

