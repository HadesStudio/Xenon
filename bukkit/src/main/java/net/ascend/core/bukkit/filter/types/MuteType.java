package net.ascend.core.bukkit.filter.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.filter.types
//   Date: Thursday, September 14, 2023 - 1:01 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MuteType {
    MUTE("Mute", "Automatically mute the sender if the text matches."),
    NONE("None", "Do not do anything if the text matches.");

    private final String display;
    private final String description;
}
