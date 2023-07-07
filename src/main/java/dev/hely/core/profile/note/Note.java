package dev.hely.core.profile.note;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name Oxygen
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
