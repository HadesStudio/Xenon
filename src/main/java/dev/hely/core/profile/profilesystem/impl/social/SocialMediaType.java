package dev.hely.core.profile.profilesystem.impl.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SocialMediaType {
    TWITTER("Twitter", "twitter.com/", "twitter.com/HelyDev"),
    YOUTUBE("YouTube", "youtube.com/", "youtube.com/HelyDev"),
    DISCORD("Discord", "", ""),
    TWITCH("Twitch", "twitch.tv/", ""),
    INSTAGRAM("Instagram", "instagram.com/", "");

    @Getter String displayName;
    @Getter String mustStartWith;
    @Getter String example;

}
