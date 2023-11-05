package dev.hely.core.bukkit.profile.squads.commands.prompts;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.profile.squads.menu.view.SquadInfoMenu;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.core.bukkit.utils.prompt.defaults.StringPrompt;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DiscordSetupPrompt extends StringPrompt {
    private final Profile profile;
    private final Squad squad;

    @Override
    public void handleBegin(Player player) {
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-discord.messages.type-name")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-discord.messages.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        if (!value.contains("discord.gg/")) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-discord.messages.invalid-link")));
            new SquadInfoMenu(profile, squad).openMenu(player);
            return null;
        }

        squad.setDiscord(value);
        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-discord.messages.setup").replace("%link%", value)));
        new SquadInfoMenu(profile, squad).openMenu(player);
        return null;
    }
}
