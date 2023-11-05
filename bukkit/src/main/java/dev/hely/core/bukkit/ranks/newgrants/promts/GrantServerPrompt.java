package dev.hely.core.bukkit.ranks.newgrants.promts;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.core.bukkit.utils.prompt.defaults.StringPrompt;
import dev.hely.lib.callback.Callback;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class GrantServerPrompt extends StringPrompt {

    private final List<String> servers;
    private final Callback<Player> callback;

    @Override
    public void handleBegin(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.server.enter-server")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public void handleCancel(Player player) {
        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.server.cancelled")) {
            player.sendMessage(CC.translate(message));
        }
    }

    @Override
    public Prompt<?> acceptInput(Player player, String value) {
        servers.add(value.trim());

        callback.callback(player);


        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.prompts.server.added")) {
            player.sendMessage(CC.translate(message).replace("%server%", value.trim()));
        }
        return null;
    }
}

