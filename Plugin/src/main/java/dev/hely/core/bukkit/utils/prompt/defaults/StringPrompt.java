package dev.hely.core.bukkit.utils.prompt.defaults;

import dev.hely.core.bukkit.utils.TextUtil;
import dev.hely.core.bukkit.utils.prompt.Prompt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class StringPrompt extends Prompt<String> {

	@Override
	public void handleFailed(Player player, String input) {
		player.sendMessage(ChatColor.RED + "Sorry, the value '" + ChatColor.WHITE + input + ChatColor.RED + "' you provided is null.");
	}

	@Override
	public String handleInput(Player player, String input) {
		if(TextUtil.isNull(input)) {
			return null;
		}
		
		return input;
	}
}
