package net.ascend.core.bukkit.filter.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MuteType {

    MUTE("Mute", "Automatically mute the sender if the text matches."),
    NONE("None", "Do not do anything if the text matches.");

    private final String display;
    private final String description;
}
