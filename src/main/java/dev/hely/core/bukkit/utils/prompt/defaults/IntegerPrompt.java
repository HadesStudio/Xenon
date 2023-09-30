package dev.hely.core.bukkit.utils.prompt.defaults;

import dev.hely.core.bukkit.utils.prompt.Prompt;
import dev.hely.lib.JavaUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class IntegerPrompt extends Prompt<Integer> {

	@Override
	public void handleFailed(Player player, String input) {
		player.sendMessage(ChatColor.RED + "Sorry, the value '" + ChatColor.WHITE + input + ChatColor.RED + "' you provided is invalid.");
	}
	
	@Override
	public Integer handleInput(Player player, String input) {
		Integer integer = JavaUtil.tryParseInt(input);
		if(integer == null) {
			return null;
		}
		
		return integer;
	}
}
