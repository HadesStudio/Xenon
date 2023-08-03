package dev.hely.core.global.profile.note.menu;

import dev.hely.core.global.profile.Profile;
import dev.hely.core.global.profile.note.Note;
import dev.hely.core.global.profile.note.menu.button.NoteButton;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
@AllArgsConstructor
public class NoteMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getName() {
        return "Notes";
    }

    @Override
    public String getTitle(Player player) {
        return "&8" + profile.getName() + " Notes (" + profile.getNotes().size() + ")";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < profile.getNotes().size(); i++) {
            Note note = profile.getNotes().get(i);

            buttons.put(i, new NoteButton(profile, i + 1, note));
        }

        return buttons;
    }
}
