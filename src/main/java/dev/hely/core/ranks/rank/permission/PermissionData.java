package dev.hely.core.ranks.rank.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */

@Getter
@Setter
@AllArgsConstructor
public class PermissionData {
    private String permission;
    private boolean global;
    private List<String> servers;
}
