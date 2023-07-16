package dev.hely.core.chat.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.chat.impl
//   Date: Sunday, July 16, 2023 - 2:13 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ChatMessage {
    @Getter private long sentDate;
    @Getter private String message;
}
