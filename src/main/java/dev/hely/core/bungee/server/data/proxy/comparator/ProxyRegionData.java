package dev.hely.core.bungee.server.data.proxy.comparator;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.data.proxy.comparator
//   Date: Monday, August 14, 2023 - 11:22 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bungee.server.data.proxy.Proxy;

import java.util.Comparator;

public class ProxyRegionData implements Comparator<Proxy> {
    @Override
    public int compare(Proxy o1, Proxy o2) {
        return Integer.compare(o1.getRegion().ordinal(),o2.getRegion().ordinal());
    }
}