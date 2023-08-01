package dev.hely.core.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.utils
//   Date: Tuesday, August 01, 2023 - 5:50 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class DurationWrapper {

    @Getter @Setter private String source;
    @Getter @Setter private Long duration;

    public boolean isPermanent() {
        return this.duration.equals((long) Integer.MAX_VALUE);
    }

    public static DurationWrapper findDurationWrapper(String source) {
        try {

            final int toReturn = TimeUtil.parseTime(source);

            if ((toReturn * 1000L) <= 0) {
                return new DurationWrapper(source, (long) Integer.MAX_VALUE);
            }

            return new DurationWrapper(source,toReturn * 1000L);
        } catch (NullPointerException | IllegalArgumentException ex) {
            return new DurationWrapper(source, (long) Integer.MAX_VALUE);
        }
    }
}
