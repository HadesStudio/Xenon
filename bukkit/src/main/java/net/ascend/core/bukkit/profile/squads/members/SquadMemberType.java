package net.ascend.core.bukkit.profile.squads.members;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SquadMemberType {
    LEADER("Leader", 0),
    CAPTAIN("Captain", 1),
    MEMBER("Member", 2);

    private final String name;
    private final int weight;
}

