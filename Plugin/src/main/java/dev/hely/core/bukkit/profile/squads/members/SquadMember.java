package dev.hely.core.bukkit.profile.squads.members;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SquadMember {
    private final UUID uuid;
    private final SquadMemberType type;
}