package net.ascend.core.bukkit.profile.profilesystem.impl.serverstats;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.impl.serverstats
//   Date: Monday, July 10, 2023 - 11:51 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;
import lombok.Setter;

public class Statistics {
    @Getter @Setter private StatisticsServer statisticServer;
    @Getter @Setter private int kills;
    @Getter @Setter private int deaths;
    @Getter @Setter private int koth_captures;
    @Getter @Setter private int citadel_captures;
    @Getter @Setter private int maps_played;

    public Statistics(StatisticsServer statisticServer) {
        this.statisticServer = statisticServer;
        this.kills = 0;
        this.deaths = 0;
        this.koth_captures = 0;
        this.citadel_captures = 0;
        this.maps_played = 0;
    }

    public void setPlayerStatistic(StatisticType statisticType, int amount) {
        switch (statisticType) {
            case KILLS:
                this.kills = amount;
            case DEATHS:
                this.deaths = amount;
            case MAPS_PLAYED:
                if (statisticServer.compatible_HCF_Core) {
                    this.maps_played = amount;
                    break;
                }
            case KOTH_CAPTURES:
                if (statisticServer.compatible_HCF_Core) {
                    this.koth_captures = amount;
                    break;
                }
                case CITADELS_CAPTURED:
                if (statisticServer.compatible_HCF_Core) {
                    this.citadel_captures = amount;
                    break;
                }
        }
    }

    public void addPlayerStatistic(StatisticType statisticType, int amount) {
        switch (statisticType) {
            case KILLS:
                this.kills += amount;
                break;
            case DEATHS:
                this.deaths += amount;
                break;
            case MAPS_PLAYED:
                if (statisticServer.compatible_HCF_Core) {
                    this.maps_played += amount;
                    break;
                }
                break;
            case KOTH_CAPTURES:
                if (statisticServer.compatible_HCF_Core) {
                    this.koth_captures += amount;
                    break;
                }
                break;
            case CITADELS_CAPTURED:
                if (statisticServer.compatible_HCF_Core) {
                    this.citadel_captures += amount;
                    break;
                }
        }
    }
}