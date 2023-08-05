package dev.hely.core.bukkit.profile.profilesystem.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum NotificationType {
    PUNISHMENT_NOTIFICATION("Punishments", Material.TNT, "Get notified when you get punished", Arrays.asList("All Punishments", "Punishment Reasons", "Punishment Durations"));

    @Getter String displayName;
    @Getter Material material;
    @Getter String slogan;
    @Getter List<String> features;
}
