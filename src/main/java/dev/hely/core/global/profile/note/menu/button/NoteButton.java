package dev.hely.core.global.profile.note.menu.button;

import dev.hely.core.global.profile.Profile;
import dev.hely.core.global.profile.note.Note;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
@AllArgsConstructor
public class NoteButton extends Button {

    private final Profile profile;
    private final int count;
    private final Note note;

    @Override
    public ItemStack getItemStack(Player player) {
        ItemMaker itemMaker = ItemMaker.of(Material.ENCHANTED_BOOK);
        itemMaker.setDisplayName("&6Note&7: &r#" + count);

        return itemMaker.build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (clickType.isLeftClick()) {
            profile.getNotes().remove(note);
            profile.save();
        }
    }
}