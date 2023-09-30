package dev.hely.core.bukkit.ranks.newgrants.promts;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.core.bukkit.utils.prompt.defaults.DurationPrompt;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.time.Duration;

@RequiredArgsConstructor
public class GrantDurationPrompt extends DurationPrompt {
    private final Profile profile;
    private final Grant grant;

    @Override
    public void handleBegin(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.duration.enter-duration")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.duration.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, Duration value) {
        grant.setDuration(value.getSeconds() * 1000L);

        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.duration.success")) {
            player.sendMessage(CC.translate(message));
        }

        return new GrantReasonPrompt(profile, grant);
    }
}
