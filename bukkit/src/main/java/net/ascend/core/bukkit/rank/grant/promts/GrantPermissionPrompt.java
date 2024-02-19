package net.ascend.core.bukkit.rank.grant.promts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.GrantScopesMenu;
import net.ascend.core.bukkit.rank.grant.menu.GrantViewSelectMenu;
import net.ascend.core.bukkit.rank.grant.types.PermissionGrant;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import net.ascend.core.bukkit.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class GrantPermissionPrompt extends StringPrompt {
    private final Profile profile;

    @Override
    public void handleBegin(Player player) {
        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.permission.enter-permission")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.permission.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        PermissionGrant permissionGrant = new PermissionGrant(UUID.randomUUID(), value.trim().replace(" ", ""), false);

        new GrantScopesMenu(new GrantViewSelectMenu(profile), profile, permissionGrant).openMenu(player);

        return null;
    }

}
