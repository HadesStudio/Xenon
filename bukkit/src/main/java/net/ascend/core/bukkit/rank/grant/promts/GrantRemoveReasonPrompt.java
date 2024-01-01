package net.ascend.core.bukkit.rank.grant.promts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.packets.GrantUpdatePacket;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import net.ascend.lib.callback.Callback;
import net.ascend.lib.CC;
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

        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.remove-reason.enter-reason")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.remove-reason.cancelled")) {
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

        XenonBukkit.getInstance().getRedisModule().sendPacket(new GrantUpdatePacket(profile.getUniqueId()));

        profile.updateGrants();
        profile.activateNextRankGrant();

        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.remove-reason.removed")) {
            player.sendMessage(CC.translate(message));
        }
        callback.callback(true);

        return null;
    }
}

