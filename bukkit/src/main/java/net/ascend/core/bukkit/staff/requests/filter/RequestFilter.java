package net.ascend.core.bukkit.staff.requests.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestFilter {
    PLAYER_NAME("Filter by player name."),
    CREATION_DATE("Filter by the most recent."),
    UNCLAIMED("Filter by requests not claimed."),
    CLAIMED("Filter by requests already claimed."),
    DEFAULT("Filter using the default method.");

    private final String type;
}
