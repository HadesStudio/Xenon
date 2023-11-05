package dev.hely.core.bukkit.profile.squads.commands.prompts;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.profile.squads.invites.SquadInvite;
import dev.hely.core.bukkit.profile.squads.menu.view.SquadInfoMenu;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.core.bukkit.utils.prompt.defaults.StringPrompt;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class JoinPasswordPrompt extends StringPrompt {
    private final Profile profile;
    private final Squad squad;
    private final SquadInvite squadInvite;

    @Override
    public void handleBegin(Player player) {
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-join.messages.type-name")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-join.messages.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        if (!squad.getPassword().equalsIgnoreCase(value)) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-join.messages.invalid-password")));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }

        profile.setSquad(squad.getName());
        squad.getMembers().add(player.getUniqueId());

        if (squadInvite != null) {
            profile.getSquadInvites().remove(squadInvite);
        }

        squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.broadcast").replace("%player%", player.getDisplayName())));
        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.joined")));

        OxygenBukkit.getInstance().getTasks().runAsync(OxygenBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();
        return null;
    }
}
