package dev.hely.core.bukkit.utils.prompt.defaults;

import java.time.Duration;
import java.time.format.DateTimeParseException;

import dev.hely.core.bukkit.utils.prompt.Prompt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class DurationPrompt extends Prompt<Duration> {

	@Override
	public void handleFailed(Player player, String input) {
		player.sendMessage(ChatColor.RED + "Sorry, the duration '" + ChatColor.RESET + input + ChatColor.RED + "' you provided is invalid.");
		player.sendMessage(ChatColor.RED + "(Example: 1h15m)");
	}
	
	@Override
	public Duration handleInput(Player player, String input) {
		if (input.equalsIgnoreCase("oo") || input.equalsIgnoreCase("infinity") || input.equalsIgnoreCase("infinite") || input.equalsIgnoreCase("permanent") || input.equalsIgnoreCase("perm") || input.equalsIgnoreCase("forever")) {
            return Duration.ofMillis(Integer.MAX_VALUE);
        }

        if (input.matches("[0-9]*")) {
            return Duration.ofSeconds(Integer.parseInt(input));
        }

        try {
            String[] parts = input.split("d");

            if (parts.length == 1) {
                return Duration.parse((input.contains("d") ? "P" : "PT") + input);
            } else {
                return Duration.parse("P" + parts[0] + "dT" + parts[1]);
            }
        } catch (DateTimeParseException e) {
            return null;
        }
	}
}
