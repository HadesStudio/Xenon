package net.ascend.core.bukkit.profile.myprofile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SocialMediaType {

    TWITTER("Twitter", "twitter.com/", "twitter.com/AscendLLC"),
    YOUTUBE("YouTube", "youtube.com/", "youtube.com/Ascend"),
    DISCORD("Discord", "", ""),
    TWITCH("Twitch", "twitch.tv/", ""),
    INSTAGRAM("Instagram", "instagram.com/", "");

    @Getter private final String displayName;
    @Getter private final String mustStartWith;
    @Getter private final String example;

}
