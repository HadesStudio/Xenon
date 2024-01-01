package net.ascend.core.bukkit.util.prompt.defaults;

import net.ascend.core.bukkit.util.prompt.Prompt;
import net.ascend.lib.JavaUtil;
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
