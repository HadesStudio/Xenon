package net.ascend.core.bukkit.tag.prompt;

import lombok.RequiredArgsConstructor;
import net.ascend.core.bukkit.lib.callback.Callback;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.menu.TagMenu;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class TagBuyPrompt extends StringPrompt {

    private final Tag tag;
    private final Callback<String> callback;

    @Override
    public void handleBegin(Player player) {
        player.closeInventory();
    }

    @Override
    public void handleCancel(Player player) {
        new TagMenu().openMenu(player);
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {

        callback.callback(value);
        return null;
    }
}
