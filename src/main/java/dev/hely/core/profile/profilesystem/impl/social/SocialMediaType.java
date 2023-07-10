package dev.hely.core.profile.profilesystem.impl.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SocialMediaType {
    TWITTER("Twitter", "MHF_Twitter", "twitter.com/", "twitter.com/HelyDev"),
    YOUTUBE("YouTube", "MHF_YouTube", "youtube.com/", "youtube.com/HelyDev"),
    DISCORD("Discord", "MHF_Discord", "", "#"),
    TWITCH("Twitch", "MHF_Twitch", "twitch.tv/", ""),
    INSTAGRAM("Instagram", "MHF_Instagram", "instagram.com/", "");

    @Getter String displayName;
    @Getter String skullOwner;
    @Getter String mustStartWith;
    @Getter String example;

}
