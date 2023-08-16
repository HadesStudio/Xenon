package dev.hely.core.bukkit.server.queue;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue
//   Date: Tuesday, August 15, 2023 - 6:02 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueuePlayer implements Comparable<QueuePlayer> {
    private UUID uuid;
    private QueueRank rank;
    private long inserted;

    @Override
    public int compareTo(QueuePlayer other) {
        int result = -Integer.compare(this.rank.getPriority(), other.getRank().getPriority());

        if(result == 0) {
            if(this.inserted < other.getInserted()) {
                return -1;
            } else {
                return 1;
            }
        }
        return result;
    }
}
