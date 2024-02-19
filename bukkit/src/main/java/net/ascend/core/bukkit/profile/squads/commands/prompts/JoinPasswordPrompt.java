package net.ascend.core.bukkit.profile.squads.commands.prompts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.profile.squads.invites.SquadInvite;
import net.ascend.core.bukkit.profile.squads.menu.view.SquadInfoMenu;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import net.ascend.core.bukkit.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class JoinPasswordPrompt extends StringPrompt {
    private final Profile profile;
    private final Squad squad;
    private final SquadInvite squadInvite;

    @Override
    public void handleBegin(Player player) {
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-join.messages.type-name")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-join.messages.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        if (!squad.getPassword().equalsIgnoreCase(value)) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-join.messages.invalid-password")));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }

        profile.setSquad(squad.getName());
        squad.getMembers().add(player.getUniqueId());

        if (squadInvite != null) {
            profile.getSquadInvites().remove(squadInvite);
        }

        squad.broadcast(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.broadcast").replace("%player%", player.getName())));
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.joined")));

        XenonBukkit.getInstance().getTaskUtil().runAsync(XenonBukkit.getInstance().getSquadManager()::saveAll);
        profile.save();
        return null;
    }
}
