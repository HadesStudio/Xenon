package dev.hely.core.bukkit.profile.settings;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.settings
//   Date: Wednesday, August 09, 2023 - 2:50 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

@Getter
@Setter
public class ProfileSettings {
    private boolean publicChatEnabled;
    private boolean receivingNewConversations;
    private boolean playingMessageSounds;
    private ChatColor color;
    private boolean tipAnnouncements;
}
