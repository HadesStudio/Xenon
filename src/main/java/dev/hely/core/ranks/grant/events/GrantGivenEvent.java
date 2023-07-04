package dev.hely.core.ranks.grant.events;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.grant.events
//   Date: Tuesday, July 04, 2023 - 5:18 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.managers.events.PluginEvents;
import dev.hely.core.profile.Profile;
import dev.hely.core.ranks.grant.Grants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
@Getter
@Setter
public class GrantGivenEvent extends PluginEvents {
    private final Grants grant;
    private final Profile target_profile;
    private final CommandSender executor;

    private boolean cancelled = false;
}