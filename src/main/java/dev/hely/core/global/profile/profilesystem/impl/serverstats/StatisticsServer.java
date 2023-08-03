package dev.hely.core.global.profile.profilesystem.impl.serverstats;

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

import dev.hely.core.global.profile.profilesystem.impl.serverstats.config.StatsConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum StatisticsServer {
    SERVER_1(StatsConfig.SERVER_1_ENABLED, StatsConfig.SERVER_1_DISPLAY_NAME, StatsConfig.SERVER_1_DISPLAY_MATERIAL, StatsConfig.SERVER_1_USE_HCF_CORE, StatsConfig.SERVER_1_INVENTORY_SLOT),
    SERVER_2(StatsConfig.SERVER_2_ENABLED, StatsConfig.SERVER_2_DISPLAY_NAME, StatsConfig.SERVER_2_DISPLAY_MATERIAL, StatsConfig.SERVER_2_USE_HCF_CORE, StatsConfig.SERVER_2_INVENTORY_SLOT),
    SERVER_3(StatsConfig.SERVER_3_ENABLED, StatsConfig.SERVER_3_DISPLAY_NAME, StatsConfig.SERVER_3_DISPLAY_MATERIAL, StatsConfig.SERVER_3_USE_HCF_CORE, StatsConfig.SERVER_3_INVENTORY_SLOT),
    SERVER_4(StatsConfig.SERVER_4_ENABLED, StatsConfig.SERVER_4_DISPLAY_NAME, StatsConfig.SERVER_4_DISPLAY_MATERIAL, StatsConfig.SERVER_4_USE_HCF_CORE, StatsConfig.SERVER_4_INVENTORY_SLOT);

    @Getter boolean enabled;
    @Getter String display_name;
    @Getter Material display_material;
    @Getter boolean compatible_HCF_Core;
    @Getter int inventory_slot;
}
