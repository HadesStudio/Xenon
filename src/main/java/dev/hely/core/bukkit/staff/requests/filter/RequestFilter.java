package dev.hely.core.bukkit.staff.requests.filter;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.staff.requests.filter
//   Date: Monday, July 31, 2023 - 3:37 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestFilter {
    PLAYER_NAME("Filter by player name."),
    CREATION_DATE("Filter by the most recent."),
    UNCLAIMED("Filter by requests not claimed."),
    CLAIMED("Filter by requests already claimed."),
    DEFAULT("Filter using the default method.");

    private final String type;
}
