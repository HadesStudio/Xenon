package dev.hely.core.bukkit.ranks.newgrants.promts;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantScopesMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantViewSelectMenu;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.core.bukkit.utils.prompt.defaults.StringPrompt;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class GrantPermissionPrompt extends StringPrompt {
    private final Profile profile;

    @Override
    public void handleBegin(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.permission.enter-permission")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.permission.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        PermissionGrant permissionGrant = new PermissionGrant(UUID.randomUUID(), value.trim().replace(" ", ""), false);

        new GrantScopesMenu(new GrantViewSelectMenu(profile), profile, permissionGrant, "permission").openMenu(player);

        return null;
    }

}
