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
public class NewSquadPrompt extends StringPrompt {
    private final Profile profile;

    @Override
    public void handleBegin(Player player) {
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.create-squad.messages.type-name")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.create-squad.messages.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        if (XenonBukkit.getInstance().getSquadManager().getSquad(value) != null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.create-squad.messages.invalid-squad")));
            new SquadMainMenu(profile).openMenu(player);
            return null;
        }

        Squad newSquad = new Squad(value, player.getUniqueId());
        newSquad.getMembers().add(player.getUniqueId());
        profile.setSquad(value);

        newSquad.setDateCreated(System.currentTimeMillis());
        XenonBukkit.getInstance().getSquadManager().getSquads().add(newSquad);

        player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.create.created")));

        XenonBukkit.getInstance().getTaskUtil().runAsync(XenonBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();

        new SquadInfoMenu(profile, newSquad).openMenu(player);
        return null;
    }
}
