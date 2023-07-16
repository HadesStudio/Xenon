package dev.hely.core.client.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.client.impl
//   Date: Saturday, July 15, 2023 - 7:55 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Version {
    UNKNOWN("Unknown",500),
    ONE_SEVEN_X("1.7.x",5),
    ONE_EIGHT_X("1.8.x",47);

    String version;
    int ProtocolNumber; //https://wiki.vg/Protocol_version_numbers
}
