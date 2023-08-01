package dev.hely.core.profile.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.menu
//   Date: Monday, July 31, 2023 - 7:56 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.profile.Profile;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public class EditProfileMenu extends Menu {
    Profile profile;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        return null;
    }
}
