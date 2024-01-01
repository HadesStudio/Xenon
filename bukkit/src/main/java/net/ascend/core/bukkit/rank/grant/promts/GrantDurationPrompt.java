package net.ascend.core.bukkit.rank.grant.promts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.DurationPrompt;
import net.ascend.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.time.Duration;

@RequiredArgsConstructor
public class GrantDurationPrompt extends DurationPrompt {
    private final Profile profile;
    private final Grant grant;

    @Override
    public void handleBegin(Player player) {
        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.duration.enter-duration")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.duration.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, Duration value) {
        grant.setDuration(value.getSeconds() * 1000L);

        for (String message : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.duration.success")) {
            player.sendMessage(CC.translate(message));
        }

        return new GrantReasonPrompt(profile, grant);
    }
}
