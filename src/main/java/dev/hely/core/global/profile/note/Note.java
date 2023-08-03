package dev.hely.core.global.profile.note;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
@Setter
public class Note {

    private UUID uniqueId;
    private String addedBy;
    private String addedReason;
    private long addedAt;
    private String addedServer;

}
