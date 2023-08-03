package dev.hely.core.global.profile.profilesystem.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.impl
//   Date: Monday, July 10, 2023 - 12:45 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum PlayerType {
    EVERYONE(ChatColor.GREEN + "Everyone", 1),
    FRIENDS_ONLY(ChatColor.AQUA + "Friends Only", 2),
    NOBODY(ChatColor.RED + "Nobody", 3);

    @Getter String displayName;
    @Getter int number;

}
