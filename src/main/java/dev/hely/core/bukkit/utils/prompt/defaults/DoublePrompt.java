package dev.hely.core.bukkit.utils.prompt.defaults;

import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.lib.JavaUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class DoublePrompt extends Prompt<Double> {

	@Override
	public void handleFailed(Player player, String input) {
		player.sendMessage(ChatColor.RED + "Sorry, the value '" + ChatColor.WHITE + input + ChatColor.RED + "' you provided is invalid.");
	}
	
	@Override
	public Double handleInput(Player player, String input) {
		Double doubl = JavaUtil.tryParseDouble(input);
		if(doubl == null) {
			return null;
		}
		
		return doubl;
	}
}
