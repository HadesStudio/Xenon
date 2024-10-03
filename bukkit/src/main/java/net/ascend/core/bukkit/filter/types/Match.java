package net.ascend.core.bukkit.filter.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Match {
    CONTAINS("Contains", "Check if the message contains the text."),
    EXACT("Exact match", "Check if the message EXACTLY matches the text."),
    REGEX("Regular Expression", "Check if the message may match the text."),
    STARTS_WITH("Starts with", "Checks if the beginning of the message starts with the text."),
    ENDS_WITH("Ends with", "Only activate if the messages end with the text.");

    private final String display;
    private final String description;
}
