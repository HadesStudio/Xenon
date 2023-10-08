package dev.hely.core.bukkit.profile.squads.commands.prompts;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.profile.squads.menu.SquadMainMenu;
import dev.hely.core.bukkit.profile.squads.menu.view.SquadInfoMenu;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.core.bukkit.utils.prompt.defaults.StringPrompt;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SearchSquadPrompt extends StringPrompt {
    private final Profile profile;

    @Override
    public void handleBegin(Player player) {
        player.closeInventory();
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.find-squad.messages.type-name")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.find-squad.messages.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        if (OxygenBukkit.getInstance().getSquadManager().getSquad(value) == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.find-squad.messages.invalid-squad")));
            new SquadMainMenu(profile).openMenu(player);
        } else {
            Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(value);
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }
        return null;
    }
}
