package net.ascend.core.bukkit.profile.squads.commands.prompts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.profile.squads.menu.SquadMainMenu;
import net.ascend.core.bukkit.profile.squads.menu.view.SquadInfoMenu;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import net.ascend.core.bukkit.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SearchSquadPrompt extends StringPrompt {
    private final Profile profile;

    @Override
    public void handleBegin(Player player) {
        player.closeInventory();
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.find-squad.messages.type-name")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.find-squad.messages.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        if (XenonBukkit.getInstance().getSquadManager().getSquad(value) == null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.find-squad.messages.invalid-squad")));
            new SquadMainMenu(profile).openMenu(player);
        } else {
            Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(value);
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }
        return null;
    }
}
