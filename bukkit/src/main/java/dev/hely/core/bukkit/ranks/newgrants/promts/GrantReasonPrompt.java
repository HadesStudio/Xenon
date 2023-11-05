package dev.hely.core.bukkit.ranks.newgrants.promts;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.packets.GrantUpdatePacket;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.core.bukkit.utils.prompt.defaults.StringPrompt;
import dev.hely.lib.menu.implement.ConfirmationMenu;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GrantReasonPrompt extends StringPrompt {

    private final Profile profile;
    private final Grant grant;

    @Override
    public void handleBegin(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.reason.enter-reason")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.reason.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        new ConfirmationMenu(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-confirm-menu.title"), (b) -> {
            if (b) {
                grant.setAddedAt(System.currentTimeMillis());
                grant.setAddedReason(value);
                profile.addGrant(grant);
                profile.save();
                OxygenBukkit.getInstance().getRedisModule().sendPacket(new GrantUpdatePacket(profile.getUniqueId()));

                profile.activateNextRankGrant();
                for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.reason.added")) {
                    player.sendMessage(CC.translate(message));
                }
            } else {
                OxygenBukkit.getInstance().getPromptManager().endPrompt(player);
                for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.reason.cancelled")) {
                    player.sendMessage(CC.translate(message));
                }
            }
        }).openMenu(player);

        return null;
    }
}
