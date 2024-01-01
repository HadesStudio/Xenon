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
public enum Match {
    CONTAINS("Contains", "Check if the message contains the text."),
    EXACT("Exact match", "Check if the message EXACTLY matches the text."),
    REGEX("Regular Expression", "Check if the message may match the text."),
    STARTS_WITH("Starts with", "Checks if the beginning of the message starts with the text."),
    ENDS_WITH("Ends with", "Only activate if the messages end with the text.");

    private final String display;
    private final String description;
}
