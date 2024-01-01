package net.ascend.core.bukkit.rank.grant.promts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.packets.GrantUpdatePacket;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.StringPrompt;
import net.ascend.lib.menu.implement.ConfirmationMenu;
import net.ascend.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GrantReasonPrompt extends StringPrompt {

    private final Profile profile;
    private final Grant grant;

    @Override
    public void handleBegin(Player player) {
        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.reason.enter-reason")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.reason.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        new ConfirmationMenu(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-confirm-menu.title"), (b) -> {
            if (b) {
                grant.setAddedAt(System.currentTimeMillis());
                grant.setAddedReason(value);
                profile.addGrant(grant);
                profile.save();
                XenonBukkit.getInstance().getRedisModule().sendPacket(new GrantUpdatePacket(profile.getUniqueId()));

                profile.activateNextRankGrant();
                for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.reason.added")) {
                    player.sendMessage(CC.translate(message));
                }
            } else {
                XenonBukkit.getInstance().getPromptManager().endPrompt(player);
                for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.reason.cancelled")) {
                    player.sendMessage(CC.translate(message));
                }
            }
        }).openMenu(player);

        return null;
    }
}
