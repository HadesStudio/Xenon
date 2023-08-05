package dev.hely.core.bukkit.profile.profilesystem.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.impl
//   Date: Monday, July 10, 2023 - 12:46 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Preferences {
    @Getter @Setter private List<UUID> ignoredPlayers = new ArrayList<>();
    @Getter @Setter private PlayerType sounds = PlayerType.EVERYONE;
    @Getter @Setter private PlayerType messages = PlayerType.EVERYONE;
    @Getter @Setter private boolean friendRequests = true;
    @Getter @Setter private PlayerType profileViewing = PlayerType.EVERYONE;
    @Getter @Setter private PlayerType toggleOfflineMessaging = PlayerType.EVERYONE;
}
