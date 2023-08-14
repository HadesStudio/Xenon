package dev.hely.core.bungee.server.data.server.comparator;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.data.server.comparator
//   Date: Monday, August 14, 2023 - 11:38 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bungee.server.data.server.Server;

import java.util.Comparator;

public class ServerPortData implements Comparator<Server> {
    @Override
    public int compare(Server o1,Server o2) {
        return Integer.compare(o1.getPort(),o2.getPort());
    }
}
