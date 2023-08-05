package dev.hely.core.api;


import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.ranks.rank.Rank;

import java.util.List;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 02/08/2023
 * Protect Name OxygenGlobal
 */

public class OxygenAPI {
    public static OxygenAPI INSTANCE;

    public OxygenAPI() {
        INSTANCE = this;
    }

    private final OxygenBukkit plugin = OxygenBukkit.getInstance();

    public Profile getProfile(UUID uuid) {
        if (plugin.getProfileManager().getProfile(uuid) == null) {
            OxygenBukkit.getInstance().debug_message("---------------------------------");
            OxygenBukkit.getInstance().debug_message("Debug Messages:");
            OxygenBukkit.getInstance().debug_message("The person's profile is null, causing an error.");
            OxygenBukkit.getInstance().debug_message("---------------------------------");
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
            OxygenBukkit.getInstance().debug_message("---------------------------------");
            OxygenBukkit.getInstance().debug_message("Debug Messages:");
            OxygenBukkit.getInstance().debug_message("Info: Attempting to getRank of " + getProfile(uuid).getName());
            OxygenBukkit.getInstance().debug_message("");
            if (this.getProfile(uuid).getActiveHighestRank() != null) {
                Rank activeHighestRank = this.getProfile(uuid).getActiveHighestRank();
                OxygenBukkit.getInstance().debug_message("Active Highest Rank: " + activeHighestRank);
            } else {
                OxygenBukkit.getInstance().debug_message("Active Highest Rank: Null (URGENT ERROR)");
            }
            if (this.getProfile(uuid).getHighestGrant() != null) {
                Grant activeHighestRank = this.getProfile(uuid).getHighestGrant();
                OxygenBukkit.getInstance().debug_message("Active Highest Grant: " + activeHighestRank);
            } else {
                OxygenBukkit.getInstance().debug_message("Active Highest Grant: Null (URGENT ERROR)");
            }
            OxygenBukkit.getInstance().debug_message("");
            OxygenBukkit.getInstance().debug_message("All Grants: ");
            if (this.getProfile(uuid).getGrants() != null) {
                for (Grant rank : this.getProfile(uuid).getGrants()) {
                    OxygenBukkit.getInstance().debug_message(" - " + rank.getRank().getName() + " Rank Grant");
                }
            } else {
                OxygenBukkit.getInstance().debug_message(" - Null (URGENT ERROR)");
            }
            OxygenBukkit.getInstance().debug_message("");
            OxygenBukkit.getInstance().debug_message("What the fuck?");
            OxygenBukkit.getInstance().debug_message("Love DevDipin");
            OxygenBukkit.getInstance().debug_message("---------------------------------");
            return new Rank("Default");
        }
        OxygenBukkit.getInstance().debug_message("---------------------------------");
        OxygenBukkit.getInstance().debug_message("Debug Messages:");
        OxygenBukkit.getInstance().debug_message("Info: Attempting to getRank of " + getProfile(uuid).getName());
        OxygenBukkit.getInstance().debug_message("");
        if (this.getProfile(uuid).getActiveHighestRank() != null) {
            Rank activeHighestRank = this.getProfile(uuid).getActiveHighestRank();
            OxygenBukkit.getInstance().debug_message("Active Highest Rank: " + activeHighestRank);
        } else {
            OxygenBukkit.getInstance().debug_message("Active Highest Rank: Null (URGENT ERROR)");
        }
        if (this.getProfile(uuid).getHighestGrant() != null) {
            Grant activeHighestRank = this.getProfile(uuid).getHighestGrant();
            OxygenBukkit.getInstance().debug_message("Active Highest Grant: " + activeHighestRank);
        } else {
            OxygenBukkit.getInstance().debug_message("Active Highest Grant: Null (URGENT ERROR)");
        }
        OxygenBukkit.getInstance().debug_message("");
        OxygenBukkit.getInstance().debug_message("All Grants: ");
        if (this.getProfile(uuid).getGrants() != null) {
            for (Grant rank : this.getProfile(uuid).getGrants()) {
                OxygenBukkit.getInstance().debug_message(" - " + rank.getRank().getName() + " Rank Grant");
            }
        } else {
            OxygenBukkit.getInstance().debug_message(" - Null (URGENT ERROR)");
        }
        OxygenBukkit.getInstance().debug_message("");
        OxygenBukkit.getInstance().debug_message("What the fuck?");
        OxygenBukkit.getInstance().debug_message("Love DevDipin");
        OxygenBukkit.getInstance().debug_message("---------------------------------");
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
