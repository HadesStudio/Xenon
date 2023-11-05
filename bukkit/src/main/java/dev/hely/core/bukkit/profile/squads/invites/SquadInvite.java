package dev.hely.core.bukkit.profile.squads.invites;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SquadInvite {
    private final String squad;
    private long timestamp = System.currentTimeMillis();
    private boolean cancelled;
}
