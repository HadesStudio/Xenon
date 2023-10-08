package dev.hely.core.bukkit.ranks.newgrants.types;

import dev.hely.core.bukkit.ranks.newgrants.Grant;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RankGrant extends Grant {
    private final String rankName;

    public RankGrant(UUID uuid, String rankName) {
        super(uuid);

        this.rankName = rankName;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof RankGrant) {
            RankGrant rankGrant = (RankGrant) o;

            return super.equals(o) && rankGrant.getRankName().equals(rankName);
        }

        return super.equals(o);
    }
}

