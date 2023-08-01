package dev.hely.core.profile.siblings;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.siblings
//   Date: Tuesday, August 01, 2023 - 4:50 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Sibling {
    private UUID uniqueId;
    private String mainProfileName;
    private UUID mainProfileUUID;
    private String siblingName;
    private UUID siblingUUID;
    private String addedBy;
    private long addedAt;
    private String addedServer;

}
