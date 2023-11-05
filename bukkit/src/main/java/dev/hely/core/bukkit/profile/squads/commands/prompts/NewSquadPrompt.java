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
public class NewSquadPrompt extends StringPrompt {
    private final Profile profile;

    @Override
    public void handleBegin(Player player) {
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.create-squad.messages.type-name")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.create-squad.messages.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        if (OxygenBukkit.getInstance().getSquadManager().getSquad(value) != null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.create-squad.messages.invalid-squad")));
            new SquadMainMenu(profile).openMenu(player);
            return null;
        }

        Squad newSquad = new Squad(value, player.getUniqueId());
        newSquad.getMembers().add(player.getUniqueId());
        profile.setSquad(value);

        newSquad.setDateCreated(System.currentTimeMillis());
        OxygenBukkit.getInstance().getSquadManager().getSquads().add(newSquad);

        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.created")));

        OxygenBukkit.getInstance().getTasks().runAsync(OxygenBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();

        new SquadInfoMenu(profile, newSquad).openMenu(player);
        return null;
    }
}
