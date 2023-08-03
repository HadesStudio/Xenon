package dev.hely.core;


import dev.hely.core.profile.Profile;
import dev.hely.core.ranks.grants.Grant;
import dev.hely.core.ranks.rank.Rank;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 02/08/2023
 * Protect Name Oxygen
 */

public class OxygenAPI {
    public static OxygenAPI INSTANCE;

    public OxygenAPI() {
        INSTANCE = this;
    }

    private final Oxygen plugin = Oxygen.getInstance();

    public Profile getProfile(UUID uuid) {
        if (plugin.getProfileManager().getProfile(uuid) == null) {
            Oxygen.getInstance().debug_message("---------------------------------");
            Oxygen.getInstance().debug_message("Debug Messages:");
            Oxygen.getInstance().debug_message("The person's profile is null, causing an error.");
            Oxygen.getInstance().debug_message("---------------------------------");
        }
        return plugin.getProfileManager().getProfile(uuid);
    }

    public String getTag(UUID uuid) {
        return this.getProfile(uuid).getTag();
    }

    public boolean isStaffChat(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getStaffProfile().isStaffChat();
    }

    public boolean isAdminChat(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getStaffProfile().isAdminChat();
    }

    public Rank getRank(UUID uuid) {
        if (this.getProfile(uuid) == null || this.getProfile(uuid).getActiveHighestRank() == null) {
            Oxygen.getInstance().debug_message("---------------------------------");
            Oxygen.getInstance().debug_message("Debug Messages:");
            Oxygen.getInstance().debug_message("Info: Attempting to getRank of " + getProfile(uuid).getName());
            Oxygen.getInstance().debug_message("");
            if (this.getProfile(uuid).getActiveHighestRank() != null) {
                Rank activeHighestRank = this.getProfile(uuid).getActiveHighestRank();
                Oxygen.getInstance().debug_message("Active Highest Rank: " + activeHighestRank);
            } else {
                Oxygen.getInstance().debug_message("Active Highest Rank: Null (URGENT ERROR)");
            }
            if (this.getProfile(uuid).getHighestGrant() != null) {
                Grant activeHighestRank = this.getProfile(uuid).getHighestGrant();
                Oxygen.getInstance().debug_message("Active Highest Grant: " + activeHighestRank);
            } else {
                Oxygen.getInstance().debug_message("Active Highest Grant: Null (URGENT ERROR)");
            }
            Oxygen.getInstance().debug_message("");
            Oxygen.getInstance().debug_message("All Grants: ");
            if (this.getProfile(uuid).getGrants() != null) {
                for (Grant rank : this.getProfile(uuid).getGrants()) {
                    Oxygen.getInstance().debug_message(" - " + rank.getRank().getName() + " Rank Grant");
                }
            } else {
                Oxygen.getInstance().debug_message(" - Null (URGENT ERROR)");
            }
            Oxygen.getInstance().debug_message("");
            Oxygen.getInstance().debug_message("What the fuck?");
            Oxygen.getInstance().debug_message("Love DevDipin");
            Oxygen.getInstance().debug_message("---------------------------------");
            return new Rank("Default");
        }
        Oxygen.getInstance().debug_message("---------------------------------");
        Oxygen.getInstance().debug_message("Debug Messages:");
        Oxygen.getInstance().debug_message("Info: Attempting to getRank of " + getProfile(uuid).getName());
        Oxygen.getInstance().debug_message("");
        if (this.getProfile(uuid).getActiveHighestRank() != null) {
            Rank activeHighestRank = this.getProfile(uuid).getActiveHighestRank();
            Oxygen.getInstance().debug_message("Active Highest Rank: " + activeHighestRank);
        } else {
            Oxygen.getInstance().debug_message("Active Highest Rank: Null (URGENT ERROR)");
        }
        if (this.getProfile(uuid).getHighestGrant() != null) {
            Grant activeHighestRank = this.getProfile(uuid).getHighestGrant();
            Oxygen.getInstance().debug_message("Active Highest Grant: " + activeHighestRank);
        } else {
            Oxygen.getInstance().debug_message("Active Highest Grant: Null (URGENT ERROR)");
        }
        Oxygen.getInstance().debug_message("");
        Oxygen.getInstance().debug_message("All Grants: ");
        if (this.getProfile(uuid).getGrants() != null) {
            for (Grant rank : this.getProfile(uuid).getGrants()) {
                Oxygen.getInstance().debug_message(" - " + rank.getRank().getName() + " Rank Grant");
            }
        } else {
            Oxygen.getInstance().debug_message(" - Null (URGENT ERROR)");
        }
        Oxygen.getInstance().debug_message("");
        Oxygen.getInstance().debug_message("What the fuck?");
        Oxygen.getInstance().debug_message("Love DevDipin");
        Oxygen.getInstance().debug_message("---------------------------------");
        return this.getProfile(uuid).getActiveHighestRank();
    }

    public List<Grant> getActiveGrants(UUID uuid) {
        return this.getProfile(uuid).getActiveGrants();
    }

    public boolean isMuted(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getActiveMute() != null;
    }

    public boolean isBanned(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getActiveBan() != null;
    }

    public boolean isBlacklisted(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getActiveBlacklist() != null;
    }
}
