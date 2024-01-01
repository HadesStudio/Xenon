package net.ascend.core.bukkit.util.prompt;

import net.ascend.core.bukkit.XenonBukkit;
import org.bukkit.entity.Player;

public abstract class Prompt<T> {

	public static Prompt<?> END_PROMPT = null;
	
	public abstract void handleBegin(Player player);
	
	public abstract void handleCancel(Player player);
	
	public abstract void handleFailed(Player player, String input);
	
	public abstract T handleInput(Player player, String input);
	
	public abstract Prompt acceptInput(Player player, T value);
	
	public void startPrompt(Player player) {
		PromptManager promptHandler = XenonBukkit.getInstance().getPromptManager();
		promptHandler.startPrompt(player, this);
	}
}
