package net.ascend.core.bukkit.rank.grant.promts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.packet.impl.GrantUpdatePacket;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import net.ascend.core.bukkit.lib.callback.Callback;
import net.ascend.core.bukkit.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GrantRemoveReasonPrompt extends StringPrompt {

    private final Grant grant;
    private final Profile profile;
    private final Callback<Boolean> callback;

    @Override
    public void handleBegin(Player player) {
        player.sendMessage(CC.translate("&aPlease type a valid duration for removing this grant in chat."));
    }

    @Override
    public void handleCancel(Player player) {
        player.sendMessage(CC.translate("&cYou have cancelled the process of removing a players grant."));

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

        player.sendMessage(CC.translate("&aYou have successfully removed the player grant!"));

        callback.callback(true);
        return null;
    }
}

