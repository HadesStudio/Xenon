package net.ascend.core.bukkit.rank.grant.promts;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.core.bukkit.util.prompt.defaults.DurationPrompt;
import net.ascend.core.bukkit.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.time.Duration;

@RequiredArgsConstructor
public class GrantDurationPrompt extends DurationPrompt {

    private final Profile profile;
    private final Grant grant;

    @Override
    public void handleBegin(Player player) {
        player.sendMessage(CC.translate("&aPlease type a valid duration for this grant in chat. &7(Example: 3d2h1m)"));
    }

    @Override
    public void handleCancel(Player player) {
        player.sendMessage(CC.translate("&cYou have cancelled the process of giving a player a grant."));
    }

    @Override
    public Prompt<?> acceptInput(Player player, Duration value) {
        grant.setDuration(value.getSeconds() * 1000L);

        player.sendMessage(CC.translate("&aYou have successfully setup a duration for the grant. &7(1/2)"));

        return new GrantReasonPrompt(profile, grant);
    }
}
