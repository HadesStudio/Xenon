package net.ascend.core.bukkit.rank.rank.menu.perks.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
// Ascend LLC Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: net.ascend.core.bukkit.rank.rank.menu.perks.impl
//   Date: Monday, January 22, 2024 - 9:10 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//   Ascend LLC Discord: https://discord.gg/xM8bktgC6q
//============================================================

import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.perks.impl.buttons.*;
import net.ascend.core.bukkit.rank.rank.perk.Perk;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class RankPerkEditSubMenu extends Menu {
    Rank rank;
    Perk perk;

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return "perkEditSub";
    }

    @Override
    public String getTitle(Player player) {
        return "&6Editing Perk: &f" + perk.getName();
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new PerkEditorNameButton(perk, rank));
        buttons.put(12, new PerkEditorDisplayNameButton(perk, rank));
        buttons.put(13, new PerkEditorItemStackButton(perk, rank));
        buttons.put(14, new PerkEditorLoreButton(perk, rank));
        buttons.put(15, new PerkEditorClickMessageButton(perk, rank));

        return buttons;
    }
}
