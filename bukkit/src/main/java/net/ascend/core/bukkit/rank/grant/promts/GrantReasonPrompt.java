package net.ascend.core.bukkit.rank.grant.promts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.packet.impl.GrantUpdatePacket;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import net.ascend.core.bukkit.lib.menu.implement.ConfirmationMenu;
import net.ascend.core.bukkit.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GrantReasonPrompt extends StringPrompt {

    private final Profile profile;
    private final Grant grant;

    @Override
    public void handleBegin(Player player) {
        player.sendMessage(CC.translate("&aPlease type a valid reason for giving this grant in chat."));
    }

    @Override
    public void handleCancel(Player player) {
        player.sendMessage(CC.translate("&cYou have cancelled the process of giving a player a grant."));
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        new ConfirmationMenu("Are you sure you want to grant?", (b) -> {
            if (b) {
                grant.setAddedAt(System.currentTimeMillis());
                grant.setAddedReason(value);
                profile.addGrant(grant);
                profile.save();

                XenonBukkit.getInstance().getRedisModule().sendPacket(new GrantUpdatePacket(profile.getUniqueId()));

                profile.activateNextRankGrant();

                player.sendMessage(CC.translate("&aYou have successfully given the player this grant!"));

            } else {
                XenonBukkit.getInstance().getPromptManager().endPrompt(player);

                player.sendMessage(CC.translate("&cYou have cancelled the process of giving a player a grant."));
            }
        }).openMenu(player);

        return null;
    }
}
