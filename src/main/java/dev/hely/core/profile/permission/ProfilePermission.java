package dev.hely.core.profile.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */

@Getter
@Setter
@AllArgsConstructor
public class ProfilePermission {

    private String permission, server;
    private boolean global;
    private long duration;

    public boolean isPermanent() {
        return duration == -1;
    }
}
