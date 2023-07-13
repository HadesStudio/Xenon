package dev.hely.core.profile.log;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 12/07/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
public class CommandLog {

    private UUID uniqueId;
    private String command;
    private String serverName;
    private long addedAt;

}
