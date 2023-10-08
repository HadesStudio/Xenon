package dev.hely.core.bukkit.ranks.newgrants.promts;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.packets.GrantUpdatePacket;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.core.bukkit.utils.prompt.defaults.StringPrompt;
import dev.hely.lib.callback.Callback;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GrantRemoveReasonPrompt extends StringPrompt {

    private final Grant grant;
    private final Profile profile;
    private final Callback<Boolean> callback;

    @Override
    public void handleBegin(Player player) {
        player.closeInventory();

        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.remove-reason.enter-reason")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.remove-reason.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
        callback.callback(true);
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        grant.setRemoved(true);
        grant.setRemovedBy(player.getUniqueId());
        grant.setRemovedReason(value);
        grant.setRemovedAt(System.currentTimeMillis());
        profile.save();

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new GrantUpdatePacket(profile.getUniqueId()));

        profile.updateGrants();
        profile.activateNextRankGrant();

        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.remove-reason.removed")) {
            player.sendMessage(CC.translate(message));
        }
        callback.callback(true);

        return null;
    }
}

