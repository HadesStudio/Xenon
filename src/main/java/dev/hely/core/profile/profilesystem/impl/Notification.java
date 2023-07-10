package dev.hely.core.profile.profilesystem.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class Notification {
    @Getter private UUID uuid;
    @Getter private long sentdate;
    @Getter private List<String> notification;
}
