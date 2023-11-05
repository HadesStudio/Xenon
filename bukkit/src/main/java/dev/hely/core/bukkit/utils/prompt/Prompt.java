package dev.hely.core.bukkit.utils.prompt;

import dev.hely.core.bukkit.OxygenBukkit;
import org.bukkit.entity.Player;

public abstract class Prompt<T> {

	public static Prompt<?> END_PROMPT = null;
	
	public abstract void handleBegin(Player player);
	
	public abstract void handleCancel(Player player);
	
	public abstract void handleFailed(Player player, String input);
	
	public abstract T handleInput(Player player, String input);
	
	public abstract Prompt acceptInput(Player player, T value);
	
	public void startPrompt(Player player) {
		PromptManager promptHandler = OxygenBukkit.getInstance().getPromptManager();
		promptHandler.startPrompt(player, this);
	}
}
