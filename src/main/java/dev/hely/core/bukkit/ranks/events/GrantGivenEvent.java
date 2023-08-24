package dev.hely.core.bukkit.ranks.events;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.ranks.events
//   Date: Thursday, August 24, 2023 - 11:20 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.ranks.rank.Rank;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class GrantGivenEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Profile profile;
    private final Rank rankGranted;
    private final Grant grant;
    private final Player giver;

    public GrantGivenEvent(Grant grant, Profile profile, Player giver) {
        this.profile = profile;
        this.grant = grant;
        this.rankGranted = grant.getRank();
        this.giver = giver;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

