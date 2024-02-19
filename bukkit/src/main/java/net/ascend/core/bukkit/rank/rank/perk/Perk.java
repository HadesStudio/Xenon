package net.ascend.core.bukkit.rank.rank.perk;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
// Ascend LLC Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: net.ascend.core.bukkit.rank.rank.perk
//   Date: Sunday, January 21, 2024 - 8:10 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//   Ascend LLC Discord: https://discord.gg/xM8bktgC6q
//============================================================

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Perk {

    private String name;
    private String material;
    private Integer materialData;
    private String displayName;
    private List<String> lore;
    private List<String> clickMessage;

}
