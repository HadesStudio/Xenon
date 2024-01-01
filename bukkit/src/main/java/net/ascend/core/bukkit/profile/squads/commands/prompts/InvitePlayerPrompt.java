package net.ascend.core.bukkit.profile.squads.commands.prompts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.profile.squads.invites.SquadInvite;
import net.ascend.core.bukkit.profile.squads.menu.view.SquadInfoMenu;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import net.ascend.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class InvitePlayerPrompt extends StringPrompt {
    private final Profile profile;
    private final Squad squad;

    @Override
    public void handleBegin(Player player) {
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-invite.messages.type-name")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-invite.messages.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        Player target = XenonBukkit.getInstance().getServer().getPlayer(value);
        if (target == null) {
            player.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED.replace("%player%", value)));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }
        Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(value);
        if (targetProfile == null) {
            player.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED.replace("%player%", value)));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }
        if (target == player) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.self")));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }
        if (squad.getMembers().contains(target.getUniqueId())) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.already-in").replace("%player%", target.getName())));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }
        if (targetProfile.getSquad() != null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.has-squad")));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }
        SquadInvite squadInvite = targetProfile.getSquadInvites().stream().filter(ci -> ci.getSquad().equalsIgnoreCase(squad.getName()) && System.currentTimeMillis() - ci.getTimestamp() <= 60000).findFirst().orElse(null);
        if (squadInvite != null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.already-invite").replace("%player%", target.getName())));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }
        targetProfile.getSquadInvites().add(new SquadInvite(squad.getName()));

        squad.broadcast(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.broadcast").replace("%player%", target.getName())));
        player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.invited").replace("%player%", target.getName())));

        target.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.invite.invite").replace("%squad%", squad.getName())));
        targetProfile.save();

        return null;
    }
}
